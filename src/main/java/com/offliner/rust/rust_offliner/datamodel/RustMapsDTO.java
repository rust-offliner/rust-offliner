package com.offliner.rust.rust_offliner.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class RustMapsDTO {

    private int seed;

    private int size;

    private String imagePath;

    @JsonProperty("seed")
    private void unpackSeed(Map<String, Integer> data) {
        setSeed(data.get("seed"));
    }

    @JsonProperty("size")
    private void unpackSize(Map<String, Integer> data) {
        setSize(data.get("size"));
    }

    @JsonProperty("url")
    private void unpackImagePath(Map<String, String> data) {
        setImagePath(data.get("url"));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
