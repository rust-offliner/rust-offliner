package com.offliner.rust.rust_offliner.map;

import com.offliner.rust.rust_offliner.exceptions.*;
import com.offliner.rust.rust_offliner.exceptions.maps.ImageExtensionNotSupportedException;
import com.offliner.rust.rust_offliner.exceptions.maps.ImageNotSquareException;
import com.offliner.rust.rust_offliner.exceptions.maps.UnprocessableMapImageException;
import com.offliner.rust.rust_offliner.interfaces.IMapDao;
import com.offliner.rust.rust_offliner.maps.MapImage;
import com.offliner.rust.rust_offliner.maps.MapManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
@Slf4j
public class MapManagerTest {

    @Autowired
    MapManager manager;

    @Autowired
    IMapDao dao;

    @BeforeEach
    void init() throws IOException {
        File f1 = new File("C:\\var\\images\\123.png");
        File f2 = new File("C:\\var\\images\\123.jpg");
        Files.deleteIfExists(f1.toPath());
        Files.deleteIfExists(f2.toPath());
    }

    @Test
    void saveWithMapSize() throws ResolutionTooSmallException, ImageNotSquareException, MapStringIsNotValidBase64Exception, UnprocessableMapImageException, PrecedentEntityNotExistsException, ImageExtensionNotSupportedException {
        long id = 123;
        File directory = new File("./");
        log.info(directory.getAbsolutePath());
        File file = new File("C:\\Users\\estra\\Documents\\rust_offliner\\rust_offliner\\src\\main\\resources\\static\\goodb64.txt");
        String b64;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            b64 = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // save it
        MapImage mapImage = new MapImage(id, b64);
        manager.userSave(id, mapImage);
        // check if it is saved
        String savedB64;
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\var\\maps\\123.png"))) {
            savedB64 = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(savedB64, "");

    }
}
