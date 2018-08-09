package com.skplanet.nlp.similarities.smooth;

import org.jblas.DoubleMatrix;

/**
 * Add One Smoothing implementation
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public class AddOneSmoothing implements Smoothing {
    //private static final Logger LOGGER = Logger.getLogger(AddOneSmoothing.class.getName());

    /**
     * Generate Smoothed Matrix
     *
     * @param matrix matrix to be smoothed
     * @return smoothed {@link org.jblas.DoubleMatrix}
     */
    @Override
    public DoubleMatrix smooth(DoubleMatrix matrix) {

        DoubleMatrix newMatrix = new DoubleMatrix(matrix.getRows(), matrix.getColumns());
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                newMatrix.put(i, j, matrix.get(i, j) + 1);
            }
        }
        return newMatrix;
    }
}
