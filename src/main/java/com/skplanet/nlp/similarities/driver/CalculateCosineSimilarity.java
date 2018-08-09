package com.skplanet.nlp.similarities.driver;

import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.similarities.CosineSimilarity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Donghun Shin / donghun.shin@sk.com
 * @since 9/4/16
 */
public class CalculateCosineSimilarity {
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("s", "source", true, "input document vector", true);
        cli.addOption("t", "matrix", true, "set of document vector", true);
        cli.parseOptions(args);

        CosineSimilarity sim = new CosineSimilarity();
        Map<String, double[]> documents = new HashMap<>();
    }
}
