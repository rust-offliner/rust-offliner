package com.offliner.rust.rust_offliner.maps;

public class ImageFormatResolver {

    public static ImageExtension resolve(byte[] arr) {
        StringBuilder arrAsString = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            arrAsString.append(arr[i]);
        }
        if (arrAsString.toString().startsWith("iVBORw0KGgo")) {
            return ImageExtension.PNG;
        }
        if (arrAsString.toString().startsWith("/9j")) {
            return ImageExtension.JPEG;
        }
        return ImageExtension.UNKNOWN;
    }
}
