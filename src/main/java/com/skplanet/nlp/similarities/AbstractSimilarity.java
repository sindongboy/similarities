package com.skplanet.nlp.similarities;

import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

/**
 * Abstract Similarity
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public abstract class AbstractSimilarity implements Similarity {
    private static final Logger LOGGER = Logger.getLogger(AbstractSimilarity.class.getName());

    /**
     * Transform a Matrix to another Matrix
     * @param termDocumentMatrix matrix to be transformed
     * @return transformed matrix
     */
    @Override
    public DoubleMatrix transform(DoubleMatrix termDocumentMatrix) {
        int numDocs = termDocumentMatrix.getColumns();
        DoubleMatrix similarityMatrix = new DoubleMatrix(numDocs, numDocs);
        for (int i = 0; i < numDocs; i++) {
            LOGGER.info("compute similarity for " + i);
            DoubleMatrix sourceDocMatrix = termDocumentMatrix.getColumn(i);
            for (int j = 0; j < numDocs; j++) {
                DoubleMatrix targetDocMatrix = termDocumentMatrix.getColumn(j);
                double simVal = computeSimilarity(sourceDocMatrix, targetDocMatrix);
                similarityMatrix.put(i, j, simVal);
            }
        }
        return similarityMatrix;
    }
}
