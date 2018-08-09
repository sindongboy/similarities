package com.skplanet.nlp.similarities.config;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/2/14.
 */
public class PropertiesException extends Exception {
    public PropertiesException() {
        super("Failed to Set Properties");
    }

    public PropertiesException(String propertiesName) {
        super("Failed to Set Properties: " + propertiesName);
    }
}
