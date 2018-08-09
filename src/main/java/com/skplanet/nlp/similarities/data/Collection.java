package com.skplanet.nlp.similarities.data;

import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.util.*;

/**
 * Collection, set of documents, Handler
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/2/14.
 */
public final class Collection {
    private static final Logger LOGGER = Logger.getLogger(Collection.class.getName());

    // document frequency map
    private Map<Integer, Double> documentFrequencyMap;

    // vocabulary
    private SortedSet<String> vocabulary;

    // vocabulary map
    private Map<Integer, String> vocabularyMap;

    // document map
    private Map<Integer, Document> documents;

    // document length
    private Map<Integer, Double> documentLength;

    // average document length
    private double aveDocumentLength = -1D;

    // raw matrix
    private DoubleMatrix rawMatrix;


    /**
     * Constructor
     */
    public Collection() {
        // vocabulary members
        vocabulary = new TreeSet<String>();
        vocabularyMap = new HashMap<Integer, String>();

        // document members
        documents = new HashMap<Integer, Document>();
        documentFrequencyMap = new HashMap<Integer, Double>();

        // collection meta
        documentLength = new HashMap<Integer, Double>();
    }

    /**
     * Add a {@link com.skplanet.nlp.similarities.data.Document}
     * @param document {@link com.skplanet.nlp.similarities.data.Document} to be added
     */
    public void add(String title, String document) {
    }

    /**
     * Load documents map ( title - content )
     * @param documents documents
     */
    public void load(Map<String, String> documents) {
        Iterator iterator = documents.entrySet().iterator();
        int id = 0;
        while (iterator.hasNext()) {
            Map.Entry map = (Map.Entry) iterator.next();
            // put title and content
            Document singleDocument = new Document((String) map.getKey(), (String) map.getValue());
            this.documents.put(id++, singleDocument);
            this.vocabulary.addAll(singleDocument.getVocabulary());
        }

        // set vocabulary map
        int vID = 0;
        for (String v : this.vocabulary) {
            this.vocabularyMap.put(vID++, v);
        }
    }

    /**
     * Get documents map
     * @return document map
     */
    public Map<Integer, Document> getDocuments() {
        return this.documents;
    }

    /**
     * Get vocabulary map
     * @return vocabulary map
     */
    public Map<Integer, String> getVocabulary() {
        return this.vocabularyMap;
    }

    /**
     * Construct Raw Term-Document Matrix
     * @return raw term document matrix
     */
    public void generateRawMatrix() {

        // ---------------------------------- //
        // create raw document matrix
        // ---------------------------------- //
        int numTerms = this.vocabulary.size();
        int numDocs = this.documents.size();
		LOGGER.info("VSIZE: " + this.vocabulary.size());
		LOGGER.info("DSIZE: " + this.documents.size());


        double[][] data = new double[numTerms][numDocs];
		LOGGER.info("DATA MATRIX CREATED");

        // for each terms
        for (int i = 0; i < numTerms; i++) {
            // for each documents
            for (int j = 0; j < numDocs; j++) {
                // get term frequency
                String term = this.vocabularyMap.get(i);
                int count = this.documents.get(j).getTermFrequency().getCount(term);
                data[i][j] = count;
            }
        }
        this.rawMatrix = new DoubleMatrix(data);

        // document length info.
        DoubleMatrix documentLengthMatrix = this.rawMatrix.columnSums();
        double lengthTotal = 0D;
        for (int i = 0; i < documentLengthMatrix.getColumns(); i++) {
            this.documentLength.put(i, documentLengthMatrix.get(0, i));
            lengthTotal += documentLengthMatrix.get(0, i);
        }

        // calculate average document length
        this.aveDocumentLength = lengthTotal / (double) numDocs;

        // document frequency
        // for each term
        for (int termIndex = 0; termIndex < this.rawMatrix.getRows(); termIndex++) {
            // for each document
            double docFreqCount = 0;
            for (int docIndex = 0; docIndex < this.rawMatrix.getColumns(); docIndex++) {
                if (this.rawMatrix.get(termIndex, docIndex) > 0) {
                    docFreqCount++;
                }
            }
            this.documentFrequencyMap.put(termIndex, docFreqCount);
        }
    }

    /**
     * Get Raw Matrix
     * @return raw matrix
     */
    public DoubleMatrix getRawMatrix() {
        if (this.rawMatrix == null) {
            LOGGER.error("Raw Matrix is not set");
            throw new NullPointerException();
        }
        return this.rawMatrix;
    }

    /**
     * Get Average Document Length
     * @return ave. document length
     */
    public double getAveDocumentLength() {
        if (this.aveDocumentLength < 0D) {
            LOGGER.error("Average document length is not set");
            throw new NullPointerException();
        }
        return this.aveDocumentLength;
    }

    /**
     * Get Within Frequency
     * @param documentId document id
     * @param termId term id
     * @return wdf - within document, query frequency
     */
    public double getWithInFrequency(int documentId, int termId) {
        DoubleMatrix documentVector = this.rawMatrix.getColumn(documentId).transpose();
        double freq = 0D;
        for (int colIndex = 0; colIndex < documentVector.getColumns(); colIndex++) {
            freq += documentVector.get(0, colIndex);
        }
        return freq;
    }

    /**
     * Get document frequency
     * @param termId term id
     * @return document frequency for the term
     */
    public double getDocumentFrequency(int termId) {
        return this.documentFrequencyMap.get(termId);
    }

    /**
     * Get Normalized Document Length
     * @param documentId document id to be length normalized
     * @return length normalized document
     */
    public double getNormalizedDocumentLength(int documentId) {
        double docLen = this.documentLength.get(documentId);
        return docLen / this.aveDocumentLength;
    }

}
