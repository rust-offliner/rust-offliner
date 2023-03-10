package com.offliner.rust.rust_offliner.maps;

import com.offliner.rust.rust_offliner.exceptions.maps.ImageExtensionNotSupportedException;
import com.offliner.rust.rust_offliner.exceptions.maps.UnprocessableMapImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class MapFileWriter {

    public MapFileWriter() {
    }

    public MapFileWriter(String systemPath, Environment env) {
        this.systemPath = systemPath;
        this.env = env;
    }

    @Autowired
    Environment env;

    String systemPath;

    public String saveImage(long id, MapImage mapImage) throws UnprocessableMapImageException, ImageExtensionNotSupportedException {
        byte[] imageByte = mapImage.getImageBytes();
        String path = systemPath + id + getExtension(imageByte).value();
        log.info(path);
        log.info(systemPath);
        File image = new File(path);
        try {
            // we need to create an empty file before actually making it an image
            image.createNewFile();
            FileOutputStream stream = new FileOutputStream(image);
            stream.write(imageByte);
            stream.close();
        } catch (Exception e) {
            throw new UnprocessableMapImageException();
        }
        return path;
    }

    private ImageExtension getExtension(byte[] imageBytes) {
        return ImageFormatResolver.resolve(imageBytes);
    }

    @PostConstruct
    private void setSystemPath() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            systemPath = env.getProperty("image.path.windows");
        } else {
        systemPath = env.getProperty("image.path.linux");
        }
        try {
            Files.createDirectories(Paths.get(systemPath));
        } catch (IOException e) {
            log.error("Path for images cannot be created");
            throw new RuntimeException(e);
        }
    }


}
