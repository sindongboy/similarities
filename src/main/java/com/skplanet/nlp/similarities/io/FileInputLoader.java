package com.skplanet.nlp.similarities.io;

import com.skplanet.nlp.config.Configuration;
import com.skplanet.nlp.similarities.config.PROP;
import com.skplanet.nlp.similarities.data.CollectionNotFoundException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load Document Collection specified by any classpath configuration
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 7/6/14.
 */
@SuppressWarnings("unused")
public class FileInputLoader extends AbstractInputLoader {
    private static final Logger LOGGER = Logger.getLogger(FileInputLoader.class.getName());

    /**
     * Default Constructor
     */
    public FileInputLoader() {
        super();
    }

	public Map<String, String> load(String filePath) {
		Map<String, String> documents = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			String line;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0) {
					continue;
				}
				if (count % 1000 == 0) {
					LOGGER.info("documents loading : " + count);
				}
				count++;

				List<String> tokens = Arrays.asList(line.split(" "));
				String documentName = tokens.get(tokens.size() - 1);
				documents.put(documentName, line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			LOGGER.error("input file not found: " + filePath, e);
		} catch (IOException e) {
			LOGGER.error("error while processing: " + filePath, e);
		}

		return documents;
	}

    /**
     * Load Collection and return Document Title - Content Map
     * @return {@link Map} of title to content
     * @throws CollectionNotFoundException
     */
    @Override
    public Map<String, String> load() throws CollectionNotFoundException {
        Map<String, String> documents = new HashMap<String, String>();

        Configuration configuration = Configuration.getInstance();
        try {
            // load configuration
            configuration.loadProperties(PROP.COLLECTION_CONFIG_NAME);
            String collectionPathName = configuration.readProperty(
                    PROP.COLLECTION_CONFIG_NAME,
                    PROP.FIELD_COLLECTION_PATH
            );

            // get the path to the collection
            File collectionDir = new File(collectionPathName);
            if (!collectionDir.isDirectory()) {
                LOGGER.error("Collection Directory required, not a file: " + collectionPathName);
                throw new CollectionNotFoundException();
            }
            if (collectionDir.listFiles() == null) {
                LOGGER.error("Collection Directory has no file: " + collectionDir.getCanonicalPath());
                throw new CollectionNotFoundException();
            }
            this.documentFileList = collectionDir.listFiles();

            // load documents
            long bTime = System.currentTimeMillis();
            LOGGER.info("collection loading ....");
            BufferedReader reader;
            for (File documentFile : documentFileList) {
                String sbuf;
                if ((int) documentFile.length() == 0) {
                    continue;
                }
                char[] cbuf = new char[(int) documentFile.length()];
                try {

                    reader = new BufferedReader(new FileReader(documentFile));
                    while (!reader.ready()) {
                    }
                    reader.read(cbuf);
                    reader.close();

                } catch (FileNotFoundException e) {
                    LOGGER.warn("document doesn't exist : " + documentFile.getName(), e);
                    System.exit(1);
                } catch (IOException e) {
                    LOGGER.warn("failed to read the document : " + documentFile.getName(), e);
                    System.exit(1);
                }
                sbuf = String.valueOf(cbuf);
                documents.put(documentFile.getName(), sbuf);
            }
            long eTime = System.currentTimeMillis();
            LOGGER.info("collection loading done in " + (eTime - bTime)  + " msec.");

        } catch (IOException e) {
            LOGGER.error("failed to read main configuration : " + PROP.MAIN_CONFIG_NAME, e);
            System.exit(1);
        }
        return documents;
    }
}
