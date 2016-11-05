package org.ranceworks.postclustering.vo;


import java.util.Map;

public class DocWordCount {
    private String id;
    private Map<String, Integer> occurence;

    public DocWordCount(String id, Map<String, Integer> occurence) {
        this.id = id;
        this.occurence = occurence;
    }
}
