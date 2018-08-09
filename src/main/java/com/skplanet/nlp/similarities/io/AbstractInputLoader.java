package com.skplanet.nlp.similarities.io;

import com.skplanet.nlp.similarities.data.CollectionNotFoundException;

import java.io.File;
import java.util.Map;

/**
 * Abstract Input Loader
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/3/14.
 */
public abstract class AbstractInputLoader implements InputLoader {

    // Document File List
    protected File[] documentFileList = null;

    /**
     * Sole Constructor
     */
    protected AbstractInputLoader() {
    }

    /**
     * Load Set of Document and return title and content map
     *
     * @return {@link java.util.Map} of title and content
     * @throws CollectionNotFoundException
     */
    @Override
    public abstract Map<String, String> load() throws CollectionNotFoundException;
}
