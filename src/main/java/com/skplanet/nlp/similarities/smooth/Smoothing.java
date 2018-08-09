package com.skplanet.nlp.similarities.smooth;

import org.jblas.DoubleMatrix;

/**
 * Interface for Smoothing
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public interface Smoothing {

    /**
     * Generate Smoothed Matrix
     * @param matrix matrix to be smoothed
     * @return smoothed {@link org.jblas.DoubleMatrix}
     */
    public DoubleMatrix smooth(DoubleMatrix matrix);
}
