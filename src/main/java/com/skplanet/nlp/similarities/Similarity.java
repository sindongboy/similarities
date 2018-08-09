package com.skplanet.nlp.similarities;

import org.jblas.DoubleMatrix;

/**
 * Similarity Interface
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public interface Similarity {

    /**
     * Transform a Matrix to another Matrix
     * @param termDocumentMatrix matrix to be transformed
     * @return transformed matrix
     */
    public DoubleMatrix transform(DoubleMatrix termDocumentMatrix);

    /**
     * Compute Similarity between two matrix
     * @param source source matrix
     * @param target target matrix
     * @return similarity score
     */
    public double computeSimilarity(DoubleMatrix source, DoubleMatrix target);
}
