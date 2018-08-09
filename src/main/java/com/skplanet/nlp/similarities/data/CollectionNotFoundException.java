package com.skplanet.nlp.similarities.data;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/2/14.
 */
public class CollectionNotFoundException extends Exception {

    public CollectionNotFoundException() {
        super("Collection Not Found");
    }

    public CollectionNotFoundException(String message) {
        super(message);
    }

}
