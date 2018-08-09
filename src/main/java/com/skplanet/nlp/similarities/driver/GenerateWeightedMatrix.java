package com.skplanet.nlp.similarities.driver;


import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.similarities.data.Collection;
import com.skplanet.nlp.similarities.data.CollectionNotFoundException;
import com.skplanet.nlp.similarities.data.Document;
import com.skplanet.nlp.similarities.io.FileInputLoader;
import com.skplanet.nlp.similarities.io.InputLoader;
import com.skplanet.nlp.similarities.weight.LSIWeighting;
import com.skplanet.nlp.similarities.weight.TFIDFWeighting;
import com.skplanet.nlp.similarities.weight.Weighting;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;

/**
 * Get Term Document Matrix
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/2/14.
 */
public final class GenerateWeightedMatrix {
    private static final Logger LOGGER = Logger.getLogger(GenerateWeightedMatrix.class.getName());

    private static final String MATRIX_OUT = "sim.mat";
    private static final String VOCABULARY_OUT = "sim.voc";

    public static final String TFIDF = "TFIDF";
    public static final String LSI = "LSI";

    /**
     * Generating Term Document Matrix
     * @param args no args
     */
    public static void main(String[] args) throws CollectionNotFoundException, IOException {

        CommandLineInterface cli = new CommandLineInterface();
	    cli.addOption("i", "input", true, "input file", true);
	    cli.addOption("o", "out", true, "output path", true);
        cli.addOption("m", "method", true, "weighting scheme", true);
        cli.parseOptions(args);

        File matrixOutFile = new File(cli.getOption("o") + "/" + MATRIX_OUT);
        File vocabularyOutFile = new File(cli.getOption("o") + "/" + VOCABULARY_OUT);

        BufferedWriter matrixWriter = new BufferedWriter(new FileWriter(matrixOutFile));
        BufferedWriter vocabularyWriter = new BufferedWriter(new FileWriter(vocabularyOutFile));

        long totalStart = System.currentTimeMillis();

        long btime, etime;
        // load raw documents
        btime = System.currentTimeMillis();
        LOGGER.info("Load Raw Documents ....");
        //InputLoader input = new ClasspathInputLoader();
	    InputLoader input = new FileInputLoader();
        Map<String, String> rawDocumentSet = input.load(cli.getOption("i"));
        etime = System.currentTimeMillis();
        LOGGER.info("Load Raw Documents done : " + timeLog(btime, etime));

        // load to collections
        btime = System.currentTimeMillis();
        LOGGER.info("Convert Raw Document to Collection ....");
        Collection collection = new Collection();
        collection.load(rawDocumentSet);
        etime = System.currentTimeMillis();
        LOGGER.info("Convert Raw Document to Collection done : " + timeLog(btime, etime));

        // generate raw matrix
        btime = System.currentTimeMillis();
        LOGGER.info("Generate Raw Term-Document Matrix ....");
        collection.generateRawMatrix();
        DoubleMatrix rawMatrix = collection.getRawMatrix();
        etime = System.currentTimeMillis();
        LOGGER.info("Generate Raw Term-Document Matrix done : " + timeLog(btime, etime));

        // check weighting scheme
        Weighting weighting = null;
        if (cli.getOption("m").toUpperCase().equals(TFIDF)) {
            LOGGER.info("Generate TFIDF Weighted Term Document Matrix ....");
            weighting = new TFIDFWeighting();
        } else if (cli.getOption("m").toUpperCase().equals(LSI)) {
            LOGGER.info("Generate LSI Weighted Term Document Matrix ....");
            weighting = new LSIWeighting();
        } else if (cli.getOption("m").toUpperCase().equals("RAW")) {
            LOGGER.info("Generate RAW Term Document Matrix");
            // nothing to be weighted
        } else {
            LOGGER.error("Weighting Method Option is incorrectly set ( TFIDF will apply by default ) : " + cli.getOption("m"));
            weighting = new TFIDFWeighting();
        }

        // weighting the raw matrix
        btime = System.currentTimeMillis();
        DoubleMatrix weightedMatrix;
        if (weighting == null) {
            weightedMatrix = rawMatrix;
        } else {
            weightedMatrix = weighting.weight(rawMatrix);
        }
        etime = System.currentTimeMillis();
        LOGGER.info("Generate " + cli.getOption("m").toUpperCase() + " Weighted Matrix done : " + timeLog(btime, etime));

        // print out the result matrix
        btime = System.currentTimeMillis();
        LOGGER.info("Print Out Result Matrix ....");
        Map<Integer, Document> documentMap = collection.getDocuments();
        for (int i = 0; i < weightedMatrix.getColumns(); i++) {
            if (i % 100 == 0) {
                LOGGER.info("processed document : " + i + " / " + weightedMatrix.getColumns());
            }
            matrixWriter.write(documentMap.get(i).getName() + " ==> ");
            double total = 0.0D;
            for (int j = 0; j < weightedMatrix.getRows(); j++) {
                BigDecimal value = new BigDecimal(weightedMatrix.get(j, i), new MathContext(3, RoundingMode.DOWN));
                total += value.doubleValue();
                if (j == weightedMatrix.getRows() - 1) {
                    matrixWriter.write(value.toString());
                    matrixWriter.newLine();
                } else {
                    matrixWriter.write(value.toString() + " ");
                }
            }
        }

        Iterator iter = collection.getVocabulary().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            vocabularyWriter.write(entry.getKey().toString() + "\t" + (String) entry.getValue());
            vocabularyWriter.newLine();
        }
        matrixWriter.close();
        vocabularyWriter.close();

        etime = System.currentTimeMillis();
        LOGGER.info("Print Out Result Matrix done : " + timeLog(btime, etime));

        long totalEnd = System.currentTimeMillis();
        LOGGER.info("All Process is finished : " + timeLog(totalStart, totalEnd));
    }

    /**
     * private constructor for not creating instance
     */
    private GenerateWeightedMatrix() {
    }

    static String timeLog(long btime, long etime) {
        return (etime - btime) + " msec.";
    }
}
