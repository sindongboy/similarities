package com.skplanet.nlp.similarities.driver;

import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.similarities.BM25Similarity;
import com.skplanet.nlp.similarities.data.Collection;
import com.skplanet.nlp.similarities.data.CollectionNotFoundException;
import com.skplanet.nlp.similarities.data.Document;
import com.skplanet.nlp.similarities.io.ClasspathInputLoader;
import com.skplanet.nlp.similarities.util.MapUtil;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/10/14.
 */
public final class GenerateBM25Table {
    // logger
    private static final Logger LOGGER = Logger.getLogger(GenerateBM25Table.class.getName());

    /**
     * Generate BM25 Similarity Matrix
     *
     * @param args argument list
     * @throws CollectionNotFoundException
     */
    public static void main(String[] args) throws CollectionNotFoundException, IOException {
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("o", "output", true, "output file name", true);
        cli.parseOptions(args);

        File matrixFile = new File(cli.getOption("o"));
        BufferedWriter matrixWriter = new BufferedWriter(new FileWriter(matrixFile));

        long bTime, eTime;

        // input loading
        LOGGER.info("Generate Raw Matrix ....");
        bTime = System.currentTimeMillis();
        ClasspathInputLoader input = new ClasspathInputLoader();
        Collection collection = new Collection();
        collection.load(input.load());
        collection.generateRawMatrix();
        eTime = System.currentTimeMillis();
        LOGGER.info("Generate Raw Matrix done : " + (eTime - bTime) + " msec.");

        LOGGER.info("Generate Similarity Table ....");
        bTime = System.currentTimeMillis();
        BM25Similarity similarity = new BM25Similarity();
        similarity.init(collection);
        DoubleMatrix simTable = similarity.transform(collection.getRawMatrix());
        eTime = System.currentTimeMillis();
        LOGGER.info("Generate Similarity Table done : " + (eTime - bTime) + " msec.");

        Map<Integer, Document> documentMap = collection.getDocuments();
        Map<Integer, String> vocabularyMap = collection.getVocabulary();

        /*
        for (int documentIndex = 0; documentIndex < simTable.getColumns(); documentIndex++) {
            String documentName = documentMap.get(documentIndex).getName();
            matrixWriter.write(documentName + " ==> ");
            for (int termIndex = 0; termIndex < simTable.getRows(); termIndex++) {
                if (termIndex == simTable.getRows() - 1) {
                    matrixWriter.write(simTable.get(termIndex, documentIndex) + "");
                } else {
                    matrixWriter.write(simTable.get(termIndex, documentIndex) + " ");
                }
            }
            matrixWriter.newLine();
        }
        */

        Map<String, Double> resultMap = new HashMap<String, Double>();
        for (int i = 0; i < simTable.getRows(); i++) {
            matrixWriter.write(documentMap.get(i).getName() + " ==> ");
            for (int j = 0; j < simTable.getColumns(); j++) {
                resultMap.put(documentMap.get(j).getName(), simTable.get(i, j));
            }
            resultMap = MapUtil.sortByValue(resultMap, MapUtil.SORT_DESCENDING);
            Iterator iter = resultMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                matrixWriter.write(entry.getKey() + ":" + entry.getValue() + " ");
            }
            matrixWriter.newLine();
        }
        matrixWriter.close();
    }

    private GenerateBM25Table() {

    }
}
