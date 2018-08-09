package com.skplanet.nlp.similarities.weight;

import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;
import org.jblas.Singular;
import org.jblas.ranges.RangeUtils;

/**
 * Latent Semantic Indexing Implementation
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/7/14.
 */
public class LSIWeighting extends AbstractWeighting {
    private static final Logger LOGGER = Logger.getLogger(LSIWeighting.class.getName());

    /** * Weighting on the given {@link org.jblas.DoubleMatrix}
     *
     * @param matrix
     * @return {@link org.jblas.DoubleMatrix}
     */
    @Override
    public DoubleMatrix weight(DoubleMatrix matrix) {
        // phase 1: Singular value decomposition
        long bTime = System.currentTimeMillis();
        LOGGER.info("begin SVD operation ....");
        // SVD U, S, T
        DoubleMatrix[] svdMatrix = Singular.sparseSVD(matrix);
        long eTime = System.currentTimeMillis();
        LOGGER.info("begin SVD operation done : " + (eTime - bTime) / 1000 + " sec.");

        // A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
        DoubleMatrix u = svdMatrix[0];
        DoubleMatrix s = svdMatrix[1];
        DoubleMatrix v = svdMatrix[2];

        // compute the value of k (ie. where to truncate)
        int k = (int) Math.floor(Math.sqrt(matrix.getColumns()));

        // make the reduced term vector ( U )
        u = u.get(RangeUtils.interval(0, u.getRows()), RangeUtils.interval(0, k));
        s = DoubleMatrix.diag(s);
        s = s.get(RangeUtils.interval(0, k), RangeUtils.interval(0, k));
        v = v.get(RangeUtils.interval(0, v.getRows()), RangeUtils.interval(0, k));

        // compose reduced vectors
        DoubleMatrix weights = u.mmul(s).mmul(v.transpose());

        // Phase 2: normalize the word scores for a single document
        for (int j = 0; j < weights.getColumns(); j++) {
            double sum = sum(weights.getColumn(j));
            for (int i = 0; i < weights.getRows(); i++) {
                weights.put(i, j, Math.abs((weights.get(i, j)) / sum));
            }
        }
        return weights;
    }

    /**
     * Column Sum of a Matrix
     *
     * @param colMatrix a column of matrix
     * @return sum of a column matrix
     */
    private double sum(DoubleMatrix colMatrix) {
        double sum = 0.0D;
        for (int i = 0; i < colMatrix.getRows(); i++) {
            sum += colMatrix.get(i, 0);
        }
        return sum;
    }
}
