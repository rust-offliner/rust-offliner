package com.offliner.rust.rust_offliner.maps;

import com.offliner.rust.rust_offliner.exceptions.MapStringIsNotValidBase64Exception;

import java.util.Base64;

public class MapImage {
    final String imageB64;
    final byte[] image;

    String path;

    public MapImage(String imageB64) throws MapStringIsNotValidBase64Exception {
        this.imageB64 = imageB64;
        this.image = decode();
    }

    private byte[] decode() throws MapStringIsNotValidBase64Exception {
        try {
            return Base64.getDecoder().decode(imageB64);
        } catch (IllegalArgumentException e) {
            // more specific exception to be handled
            throw new MapStringIsNotValidBase64Exception("Map you provided is not valid!");
        }
    }
}
