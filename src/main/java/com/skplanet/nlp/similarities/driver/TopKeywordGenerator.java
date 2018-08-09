package com.skplanet.nlp.similarities.driver;

import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.similarities.util.MapUtil;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Load Term-Document Matrix and print out top n keyword for each document
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/5/14.
 */
public final class TopKeywordGenerator {
    private static final Logger LOGGER = Logger.getLogger(TopKeywordGenerator.class.getName());

    public static void main(String[] args) throws IOException {

        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("m", "matrix", true, "matrix file", true);
        cli.addOption("v", "vocabulary", true, "vocabualry file", true);
        cli.addOption("n", "num", true, "number of top keyword to display", true);
        cli.addOption("o", "output", true, "output file", true);
        cli.parseOptions(args);

        long btime, etime;

        int numKeyword = Integer.parseInt(cli.getOption("n"));
        File matrixFile = new File(cli.getOption("m"));
        File vocabularyMapFile = new File(cli.getOption("v"));
        File outputFile = new File(cli.getOption("o"));

        Map<Integer, String> vocabularyMap = new HashMap<Integer, String>();
        Map<Integer, Double> scoreMap = new HashMap<Integer, Double>();
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
        DoubleMatrix rawMatrix = new DoubleMatrix();
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }

            String[] fields = line.split(" ==> ");

            String documentName = fields[0].trim();
            writer.write(documentName + " ==> ");

            int vocabularyId = 0;
            for (String scoreString : fields[1].trim().split(" ")) {
                scoreMap.put(vocabularyId, Double.parseDouble(scoreString));
                vocabularyId++;
            }

            scoreMap = MapUtil.sortByValue(scoreMap, MapUtil.SORT_DESCENDING);

            Iterator iter = scoreMap.entrySet().iterator();
            int count = 0;
            while (iter.hasNext()) {
                if (count > numKeyword) {
                    break;
                }
                Map.Entry map = (Map.Entry) iter.next();
                writer.write(vocabularyMap.get(map.getKey()) + ":" + map.getValue() + " ");
                count++;
            }
            writer.newLine();
        }
        etime = System.currentTimeMillis();
        LOGGER.info("load matrix done : " + time(btime, etime));
        reader.close();

        writer.close();
    }

    static String time(long btime, long etime) {
        return (etime - btime) + " msec.";
    }

    /**
     * private constructor for not instantiating driver class
     */
    private TopKeywordGenerator() {
    }

}
