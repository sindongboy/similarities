package com.skplanet.nlp.similarities;

import com.skplanet.nlp.similarities.data.Collection;
import com.skplanet.nlp.similarities.util.MathUtil;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

/**
 * BM25 Similarity Scoring Class
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/8/14.
 */
public class BM25Similarity extends AbstractSimilarity {
    private static final Logger LOGGER = Logger.getLogger(BM25Similarity.class.getName());

    // k1
    private double k_1 = 1.2d;
    // k2
    private double k_3 = 8d;
    // b
    private double b;
    // collection
    private Collection collection;
    // average document length
    //private double averageDocumentLength;
    // number of document
    private int numberOfDocuments;

    /**
     * Constructor.
     */
    public BM25Similarity() {
        b = 0.75d;
    }

    /**
     * Initialize BM25 parameters
     * @param collection {@link Collection}
     */
    public void init(Collection collection) {
        this.collection = collection;
        //this.averageDocumentLength = collection.getAveDocumentLength();
        this.numberOfDocuments = collection.getDocuments().size();
    }

    /**
     * Transform a Matrix to another Matrix
     *
     * @param termDocumentMatrix matrix to be transformed
     * @return transformed matrix
     */
    @Override
    public DoubleMatrix transform(DoubleMatrix termDocumentMatrix) {
        long bTime, eTime;
        int numDocs = termDocumentMatrix.getColumns();
        DoubleMatrix similarityMatrix = new DoubleMatrix(numDocs, numDocs);
        for (int i = 0; i < numDocs; i++) {
            bTime = System.currentTimeMillis();
            DoubleMatrix sourceDocMatrix = termDocumentMatrix.getColumn(i);
            for (int j = 0; j < numDocs; j++) {
                DoubleMatrix targetDocMatrix = termDocumentMatrix.getColumn(j);
                double simVal = computeSimilarity(sourceDocMatrix, targetDocMatrix, j);
                similarityMatrix.put(i, j, simVal);
            }
            eTime = System.currentTimeMillis();
            LOGGER.info("compute similarity for " + i + "(" + (eTime - bTime) + " msec." + ")");
        }
        return similarityMatrix;
    }

    /**
     * Uses BM25 to compute a weight for a term in a document.
     * @param wdf The term frequency in the document
     * @param normDocumentLength the normalized document's length
     * @param df The document frequency of the term
     * @param wqf the term frequency in the query
     * @return the score assigned by the weighting model BM25.
     */
    private final double score( double wdf, double normDocumentLength, double df, double wqf) {
        double K = k_1 * ((1 - b) + b * normDocumentLength) + wdf;
        return MathUtil.log2((numberOfDocuments - df + 0.5d) / (df + 0.5d)) * ((k_1 + 1d) * wdf / (K + wdf)) * ((k_3 + 1) * wqf / (k_3 + wqf));
    }

    /**
     * Compute Similarity between two matrix
     *
     * @param source source matrix
     * @param target target matrix
     * @return similarity score
     */
    @Deprecated
    @Override
    public double computeSimilarity(DoubleMatrix source, DoubleMatrix target) {
        LOGGER.error("BM25 use other similarity method \"computeSimilarity(source, target, target id)\"");
        return 0D;
    }

    /**
     * BM25 use this method to compute similarity
     * @param source source document
     * @param target target document
     * @param targetDocID target document id
     * @return document to document similarity score
     */
    public double computeSimilarity(DoubleMatrix source, DoubleMatrix target, int targetDocID) {
        double sim = 0D;
        for (int srcTermIndex = 0; srcTermIndex < source.getRows(); srcTermIndex++) {
            if(source.get(srcTermIndex, 0) > 0) {
                // tf in the target document
                double wdf = target.get(srcTermIndex, 0);
                double normDocumentLength = this.collection.getNormalizedDocumentLength(targetDocID);
                double df = this.collection.getDocumentFrequency(srcTermIndex);
                double wqf = source.get(srcTermIndex, 0);
                sim += score(wdf, normDocumentLength, df, wqf);
            }
        }
        return sim;
    }
}
