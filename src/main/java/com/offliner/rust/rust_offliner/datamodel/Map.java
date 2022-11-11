package com.offliner.rust.rust_offliner.datamodel;

import java.io.File;
import java.util.List;

public class Map {

    private final int size;
    private final String imagePath;
    private final File image;
    private List<Base> baseList;

    public Map(int size, String imagePath) {
        this.size = size;
        this.imagePath = imagePath;
        this.image = this.getImage();
    }

    public int getSize() {
        return size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public File getImage() {
        return new File(this.imagePath);
    }
}
