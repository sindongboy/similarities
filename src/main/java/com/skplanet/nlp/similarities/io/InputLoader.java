package com.skplanet.nlp.similarities.io;

import com.skplanet.nlp.similarities.data.CollectionNotFoundException;

import java.util.Map;

/**
 * Input Loader Interface
 *
 * @author Donghun Shin, donghun.shin@sk.com
 */
@SuppressWarnings("unused")
public interface InputLoader {

    /**
     * load set of documents
     */
    public Map<String, String> load() throws CollectionNotFoundException;

	/**
	 * Load Set of Documents from a given file
	 * @param filePath path to the input corpus
	 * @return document name and body pair
	 * @throws CollectionNotFoundException
	 */
	public Map<String, String> load(String filePath) throws CollectionNotFoundException;

}
