package state;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;
import com.offliner.rust.rust_offliner.state.TrackableServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackableServerTest {

    private TrackableServer server;

    @BeforeEach
    void init() {
        server = new TrackableServer(123, new EServerDto(123), 1, Instant.now());
    }

    @Test
    void decrementIsOk() {
        server.decrement();
        assertEquals(server.getCount(), 0);
    }
}
