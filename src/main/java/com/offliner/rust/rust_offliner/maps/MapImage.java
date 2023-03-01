package com.offliner.rust.rust_offliner.maps;

import com.offliner.rust.rust_offliner.exceptions.ImageNotSquareException;
import com.offliner.rust.rust_offliner.exceptions.MapStringIsNotValidBase64Exception;
import com.offliner.rust.rust_offliner.exceptions.ResolutionTooSmallException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Getter
public class MapImage {

    final long id;
    final String imageB64;
    final byte[] imageBytes;

    String path;

    public MapImage(long id, String imageB64) throws MapStringIsNotValidBase64Exception, ResolutionTooSmallException, ImageNotSquareException {
        this.id = id;
        this.imageB64 = imageB64;
        this.imageBytes = decode();
        validateResolution();
//        this.image = image;
    }

    private byte[] decode() throws MapStringIsNotValidBase64Exception {
        try {
            return Base64.getDecoder().decode(imageB64);
        } catch (IllegalArgumentException e) {
            // more specific exception to be handled
            throw new MapStringIsNotValidBase64Exception("Map you provided is not valid!");
        }
    }

    private void validateResolution() throws ResolutionTooSmallException, ImageNotSquareException {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            // check if image is square or almost square
            if (image.getWidth() < 900) {
                throw new ResolutionTooSmallException();
            }
            if ((Math.abs(image.getHeight() - image.getWidth()) > 50)) {
                throw new ImageNotSquareException();
            }
        } catch (IOException e) {
            log.error("Image bytes are null");
        }
    }

}
