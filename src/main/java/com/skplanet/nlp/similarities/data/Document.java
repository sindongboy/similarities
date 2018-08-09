package com.skplanet.nlp.similarities.data;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;

import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public class Document {
    // logger
    //private static final Logger LOGGER = Logger.getLogger(Document.class.getName());

    // document name
    private String name;
    // raw document text
    private String rawText;
    // term frequency
    private Bag<String> termFrequency;

    /**
     * Constructor
     */
    public Document() {
    }

    /**
     * Constructor with title and content
     * @param title title
     * @param content content
     */
    public Document(String title, String content) {
        this.name = title;
        this.rawText = content;
        tokenize(content);
    }

    /**
     * Set Document Title
     * @param name document title
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Document Title
     * @return document title
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set Document Text
     * @param text document text
     */
    public void setText(String text) {
        this.rawText = text;
        tokenize(text);
    }

    /**
     * Get Document Text
     * @return document text
     */
    public String getText() {
        return this.rawText;
    }

    /**
     * Get Vocabulary
     * @return vocabulary - unique set of terms in a document
     */
    public Set<String> getVocabulary() {
        return this.termFrequency.uniqueSet();
    }

    /**
     * Get Term Frequency Information
     * @return term frequency
     */
    public Bag<String> getTermFrequency() {
        return this.termFrequency;
    }

    /**
     * White Space Tokenize
     * @param text text to be tokenize
     */
    private void tokenize(String text) {
        if (this.termFrequency == null) {
            this.termFrequency = new HashBag<String>();
        }

        StringTokenizer tokens = new StringTokenizer(text, " \n\r");
        while (tokens.hasMoreTokens()) {
            String term = tokens.nextToken().trim();
            if (term.length() == 0) {
                continue;
            }
            this.termFrequency.add(term);
        }
    }
}
