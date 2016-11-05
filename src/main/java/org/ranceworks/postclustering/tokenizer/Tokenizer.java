package org.ranceworks.postclustering.tokenizer;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public interface Tokenizer {
    default public List<String> tokenize(String text) throws IOException {
        return Arrays.asList(text.split("\\s+"));
    }
}
