package com.skplanet.nlp.similarities.driver;

import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.similarities.BM25Similarity;
import com.skplanet.nlp.similarities.CosineSimilarity;
import com.skplanet.nlp.similarities.JaccardSimilarity;
import com.skplanet.nlp.similarities.Similarity;
import com.skplanet.nlp.similarities.util.MapUtil;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.io.*;
import java.util.*;

/**
 * Load Term-Document Matrix and print out top n keyword for each document
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/5/14.
 */
public final class GenerateSimilarityTable {
    private static final Logger LOGGER = Logger.getLogger(GenerateSimilarityTable.class.getName());

    public static void main(String[] args) throws IOException {

        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("m", "matrix", true, "matrix file", true);
        cli.addOption("v", "vocabulary", true, "vocabulary file", true);
        cli.addOption("s", "similarity", true, "similarity measure", true);
        cli.addOption("o", "output", true, "output file", true);
        cli.parseOptions(args);

        long btime, etime;

        File matrixFile = new File(cli.getOption("m"));
        File vocabularyMapFile = new File(cli.getOption("v"));
        File outputFile = new File(cli.getOption("o"));

        Map<Integer, String> vocabularyMap = new HashMap<Integer, String>();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        BufferedReader reader;
        String line;

        // load vocabulary map
        LOGGER.info("load vocabulary ....");
        btime = System.currentTimeMillis();
        reader = new BufferedReader(new FileReader(vocabularyMapFile));
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }
            String[] fields = line.split("\\t");
            vocabularyMap.put(Integer.parseInt(fields[0].trim()), fields[1].trim());
        }
        reader.close();
        etime = System.currentTimeMillis();
        LOGGER.info("load vocabulary done : " + time(btime, etime));

        // load matrix
        reader = new BufferedReader(new FileReader(matrixFile));
        LOGGER.info("load matrix ....");
        btime = System.currentTimeMillis();
        Map<Integer, String> documentMap = new HashMap<Integer, String>();
        int rowCount = 0, colCount = 0;
        List<Double> valueList = new ArrayList<Double>();
        int documentId = 0;
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }
            String[] fields = line.split(" ==> ");

            documentMap.put(rowCount, fields[0].trim());

            if (colCount == 0) {
                colCount = fields[1].trim().split(" ").length;
            }
            for (String v : fields[1].trim().split(" ")) {
                valueList.add(Double.parseDouble(v));
            }
            rowCount++;
        }
        etime = System.currentTimeMillis();
        LOGGER.info("load matrix done : " + time(btime, etime));
        reader.close();

        DoubleMatrix rawMatrix = new DoubleMatrix(rowCount, colCount);

        int count = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                rawMatrix.put(i, j, valueList.get(count));
                count++;
            }
        }

        // check similarity scheme
        Similarity sim = null;
        if (cli.getOption("s").toUpperCase().equals("COSINE")) {
            LOGGER.info("Generate similarity table using cosine similarity ");
            sim = new CosineSimilarity();
        } else if (cli.getOption("s").toUpperCase().equals("JACCARD")) {
            LOGGER.info("Generate similarity table using jaccard similarity ");
            sim = new JaccardSimilarity();
        } else if (cli.getOption("s").toUpperCase().equals("BM25")) {
            LOGGER.info("Generate similarity table using bm25 similarity ");
            sim = new BM25Similarity();
        } else {
            sim = new CosineSimilarity();
        }

        rawMatrix = rawMatrix.transpose();
        DoubleMatrix simMatrix = sim.transform(rawMatrix);

        Map<String, Double> resultMap = new HashMap<String, Double>();
        for (int i = 0; i < simMatrix.getRows(); i++) {
            writer.write(documentMap.get(i) + " ==> ");
            for (int j = 0; j < simMatrix.getColumns(); j++) {
                resultMap.put(documentMap.get(j), simMatrix.get(i, j));
            }
            resultMap = MapUtil.sortByValue(resultMap, MapUtil.SORT_DESCENDING);
            Iterator iter = resultMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                writer.write(entry.getKey() + ":" + entry.getValue() + " ");
            }
            writer.newLine();
        }
        writer.close();
    }

    static String time(long btime, long etime) {
        return (etime - btime) + " msec.";
    }

    /**
     * private constructor for not instantiating driver class
     */
    private GenerateSimilarityTable() {
    }

}
