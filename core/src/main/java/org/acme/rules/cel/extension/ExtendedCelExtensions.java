package org.acme.rules.cel.extension;

public class ExtendedCelExtensions {

    public static ExtendedCelListExtensions list() {
        return new ExtendedCelListExtensions();
    }
    public static ExtendedCelTimestampExtensions timestamp() {
        return new ExtendedCelTimestampExtensions();
    }
    
}
