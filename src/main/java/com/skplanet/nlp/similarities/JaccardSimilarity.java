package com.skplanet.nlp.similarities;

import org.jblas.DoubleMatrix;

/**
 * Jaccard Document to Document Similarity Matrix Generator
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public class JaccardSimilarity extends AbstractSimilarity {
    /**
     * Compute Similarity between two matrix
     *
     * @param source source matrix
     * @param target target matrix
     * @return similarity score
     */
    @Override
    public double computeSimilarity(DoubleMatrix source, DoubleMatrix target) {
        double intersection = 0.0D;
        for (int i = 0; i < source.getRows(); i++) {
            intersection += Math.min(source.get(i, 0), target.get(i, 0));
        }

        if (intersection > 0.0D) {
            double union = source.norm1() + target.norm1() - intersection;
            return intersection / union;
        } else {
            return 0.0D;
        }
    }
}
