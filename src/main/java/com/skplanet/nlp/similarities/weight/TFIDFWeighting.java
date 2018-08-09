package com.skplanet.nlp.similarities.weight;

import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;


/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/2/14.
 */
public class TFIDFWeighting extends AbstractWeighting {

    private static Logger LOGGER = Logger.getLogger(TFIDFWeighting.class.getName());

    /**
     * Constructor
     */
    public TFIDFWeighting() {

    }

    /**
     * Weighting on the given {@link org.jblas.DoubleMatrix}
     *
     * @param matrix raw matrix to be weighted
     * @return {@link org.jblas.DoubleMatrix}
     */
    @Override
    public DoubleMatrix weight(DoubleMatrix matrix) {
        // Phase 1: apply IDF weight to the raw word frequencies
        int columnDimension = matrix.getColumns();
        int rowDimension = matrix.getRows();
        long total = 0;
        int numDoc = columnDimension;
        for (int j = 0; j < columnDimension; j++) {
            long bTime = System.currentTimeMillis();
            for (int i = 0; i < rowDimension; i++) {
                double matrixElement = matrix.get(i, j);
                if (matrixElement > 0.0D) {
                    // get document frequency for the current term
                    double dm = sumDocumentFrequency(matrix.getRow(i));
                    // apply IDF weight in i-th row, j-th column element
                    matrix.put(i, j, matrix.get(i, j) * (1 + Math.log(columnDimension) - Math.log(dm)));
                }
            }
            long eTime = System.currentTimeMillis();
            total += eTime - bTime;
            if (j % 100 == 0) {
                LOGGER.info("document : " + j + "/" + numDoc + " (" + total + " msec)");
            }
        }

        // Phase 2: normalize the word scores for a single document
        for (int j = 0; j < matrix.getColumns(); j++) {
            double sum = sum(matrix.getColumn(j));
            for (int i = 0; i < matrix.getRows(); i++) {
                matrix.put(i, j, matrix.get(i, j) / sum);
            }
        }
        return matrix;

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

    /**
     * Number of documents with the term occurred
     *
     * @param rowMatrix row matrix
     * @return Number of documents with the term occurred
     */
    private double sumDocumentFrequency(DoubleMatrix rowMatrix) {
        double sum = 0.0D;
        for (int j = 0; j < rowMatrix.getColumns(); j++) {
            if (rowMatrix.get(0, j) > 0.0D) {
                sum++;
            }
        }
        return sum;
    }
}
