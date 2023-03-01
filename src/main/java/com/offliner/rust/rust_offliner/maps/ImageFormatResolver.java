package com.offliner.rust.rust_offliner.maps;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class ImageFormatResolver {

    public static ImageExtension resolve(byte[] arr) {
//        StringBuilder arrAsString = new StringBuilder();
        byte[] firstbytes = new byte[8];
        for (int i = 0; i < 8; ++i) {
            firstbytes[i] = arr[i];
        }
        String b64 = Base64.getEncoder().encodeToString(firstbytes);
        log.info(String.valueOf(b64));
        if (b64.toString().startsWith("iVBORw0KGgo")) {
            return ImageExtension.PNG;
        }
        if (b64.toString().startsWith("/9j")) {
            return ImageExtension.JPEG;
        }
        return ImageExtension.UNKNOWN;
    }
}
