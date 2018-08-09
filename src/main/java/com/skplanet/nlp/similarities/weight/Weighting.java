package com.skplanet.nlp.similarities.weight;

import org.jblas.DoubleMatrix;

/**
 * Weighting Interface
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/2/14.
 */
public interface Weighting {

    /**
     * Weighting on the given {@link org.jblas.DoubleMatrix}
     * @param matrix
     * @return {@link org.jblas.DoubleMatrix}
     */
    public DoubleMatrix weight(DoubleMatrix matrix);
}
