package com.skplanet.nlp.similarities;

import org.jblas.DoubleMatrix;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public class CosineSimilarity extends AbstractSimilarity {
    //private static final Logger LOGGER = Logger.getLogger(CosineSimilarity.class.getName());
    /**
     * Compute Similarity between two matrix
     *
     * @param source source matrix
     * @param target target matrix
     * @return similarity score
     */
    @Override
    public double computeSimilarity(DoubleMatrix source, DoubleMatrix target) {
        double dotProduct = source.dot(target);
        double euclideanDist = source.norm2() * target.norm2();
        return dotProduct / euclideanDist;
    }
}
