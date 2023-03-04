package com.offliner.rust.rust_offliner.map;

import com.offliner.rust.rust_offliner.exceptions.*;
import com.offliner.rust.rust_offliner.maps.MapImage;
import com.offliner.rust.rust_offliner.maps.MapFileWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class MapFileWriterTest {

    Environment env;

    MapFileWriter manager;

    @BeforeEach
    public void init() throws IOException {
        File f = new File("C:\\var\\images\\123");
        Files.deleteIfExists(f.toPath());
        manager = new MapFileWriter("C:\\var\\maps\\", env);
    }

    // TODO TESTS

    @Test
    public void saveRectangleImageThrowsIsNotSquareException() {
        assertThrows(ImageNotSquareException.class, () -> {
            // "test" file to B64
            File directory = new File("./");
            log.info(directory.getAbsolutePath());
            File file = new File("C:\\Users\\estra\\Documents\\rust_offliner\\rust_offliner\\src\\main\\resources\\static\\squareb64.txt");
            String b64;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                b64 = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // save it
            MapImage mapImage = new MapImage(123, b64);
            manager.saveImage(mapImage.getId(), mapImage);

        });
    }

    @Test
    public void saveRectangleImageThrowsResolutionIsTooSmallException() {
        assertThrows(ResolutionTooSmallException.class, () -> {
            // "test" file to B64
            File directory = new File("./");
            log.info(directory.getAbsolutePath());
            File file = new File("C:\\Users\\estra\\Documents\\rust_offliner\\rust_offliner\\src\\main\\resources\\static\\smallb64.txt");
            String b64;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                b64 = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // save it
            MapImage mapImage = new MapImage(123, b64);

        });
    }

    @Test
    // this checks only invalid characters because there is no way to check for actual image validity
    public void saveRectangleImageThrowsInvalidBase64Exception() throws ResolutionTooSmallException, ImageNotSquareException, MapStringIsNotValidBase64Exception, UnprocessableMapImageException, ImageExtensionNotSupportedException {
        assertThrows(MapStringIsNotValidBase64Exception.class, () -> {
            // "test" file to B64
            manager = new MapFileWriter("C:\\var\\maps\\", env);
            File directory = new File("./");
            log.info(directory.getAbsolutePath());
            File file = new File("C:\\Users\\estra\\Documents\\rust_offliner\\rust_offliner\\src\\main\\resources\\static\\invalidb64.txt");
            String b64;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                b64 = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // save it
            MapImage mapImage = new MapImage(1243, b64);
        });
    }



}
