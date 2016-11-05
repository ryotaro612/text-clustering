package org.ranceworks.postclustering.vo;
import org.apache.spark.mllib.linalg.Vector;

public class DocVector {
    private final String id;
    private final Vector vector;


    public DocVector(String id, Vector vector) {
        this.id = id;
        this.vector = vector;
    }
}
