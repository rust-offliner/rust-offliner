package map;

import com.offliner.rust.rust_offliner.exceptions.*;
import com.offliner.rust.rust_offliner.maps.MapImage;
import com.offliner.rust.rust_offliner.maps.MapManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class MapManagerTest {

    Environment env;

    MapManager manager;

    @BeforeEach
    public void init() throws IOException {
        File f = new File("C:\\var\\images\\123");
        Files.deleteIfExists(f.toPath());
        manager = new MapManager("C:\\var\\maps\\", env);
    }

    @Test
    public void saveImage() throws UnprocessableMapImageException, ImageExtensionNotSupportedException, ResolutionTooSmallException, ImageNotSquareException, MapStringIsNotValidBase64Exception {
        // "test" file to B64
        File directory = new File("./");
        log.info(directory.getAbsolutePath());
        File file = new File("C:\\Users\\estra\\Documents\\rust_offliner\\rust_offliner\\src\\main\\resources\\static\\problemb64.txt");
        String b64;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            b64 = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // save it
        MapImage mapImage = new MapImage(123, b64);
//        byte[] bytes = Base64.getDecoder().decode(b64);
        manager.saveImage(mapImage.getId(), mapImage);
        // check if it is saved
        String savedB64;
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\var\\maps\\123.jpg"))) {
            savedB64 = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(savedB64, "");
    }
}
