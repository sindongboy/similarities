package com.skplanet.nlp.similarities.util;

import org.jblas.DoubleMatrix;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/8/14.
 */
public final class MathUtil {
    //private static final Logger LOGGER = Logger.getLogger(MathUtil.class.getName());

    /**
     * The constant for Log 2 base change
     */
    public static final double LOG2_E = Math.log(2.0D);
    /**
     * The reciprocal of CONSTANT, computed for efficiency.
     */
    public static final double RLOG2_E = 1.0D / LOG2_E;


    /**
     * Get base 2 log value of the given double value
     * @param v double value
     * @return base 2 log value
     */
    public static final double log2(double v) {
        return (Math.log(v) * RLOG2_E);
    }

    /**
     * Get the sum of the given column
     * @param matrix matrix
     * @param i column index
     * @return column sum of the given column index
     */
    public static final double getColumnSum(DoubleMatrix matrix, int i) {
        return matrix.columnSums().get(0, i);
    }

    private MathUtil() {

    }

}
