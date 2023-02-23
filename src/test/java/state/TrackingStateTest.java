package state;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.state.TrackingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TrackingStateTest {

//    @Mock
//    private Object lock;

    private TrackingState state;

    @BeforeEach
    void init() {
        state = new TrackingState(new Object());
    }

    @Test
    void addingToAnEmptyList() throws KeyAlreadyExistsException {
        long id = 123; //some id to store
        state.add(id);
        assertTrue(state.contains(id));
    }

    @Test
    void addingSameIdToListWhichAlreadyContainsItThrowsException() throws KeyAlreadyExistsException {
        long id = 123;
        assertThrows(KeyAlreadyExistsException.class, () -> {
            state.add(id);
            state.add(id);
        });
    }

    @Test
    void getFromAnEmptyListThrowsException() {
        long id = 123;
        assertThrows(ServerNotTrackedException.class, () -> {
            state.getById(id);
        });
    }

    @Test
    void getByIdFromListIsOk() throws KeyAlreadyExistsException, ServerNotTrackedException {
        long id = 123;
        state.add(id);
        EServerDto server = state.getById(id).getData();
        assertEquals(server.getId(), id);
    }

    @Test
    void getByPositionFromListIsOk() throws KeyAlreadyExistsException {
        long id1 = 123;
        long id2 = 234;
        long id3 = 345;

        state.add(id1);
        state.add(id2);
        state.add(id3);

        assertEquals(state.getByPosition(0).getId(), id1);
        assertEquals(state.getByPosition(1).getId(), id2);
        assertEquals(state.getByPosition(2).getId(), id3);
    }

    @Test
    void getByPositionOutsideBoundariesThrowsException() throws KeyAlreadyExistsException {
        long id = 123;

        state.add(id);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            state.getByPosition(1);
        });
    }

    @Test
    void replaceByNonexistentIdThrowsException() {
        long id = 123;
        EServerDto serverDTO = new EServerDto(id);
        assertThrows(ServerNotTrackedException.class, () -> {
           state.replace(id, serverDTO);
        });
    }

    @Test
    void replaceByIdIsOk() throws KeyAlreadyExistsException, ServerNotTrackedException {
        long id = 123;
        EServerDto toReplace = new EServerDto(id, "test");
        EServerDto oldServer = state.add(id);
        state.replace(id, toReplace);
        EServerDto newServer = state.getById(id).getData();

        // meaning data is not equal
        assertNull(oldServer.getName());
        assertNotNull(newServer.getName());
    }

    @Test
    void removeByIdIsOk() throws KeyAlreadyExistsException, ServerNotTrackedException {
        long id = 123;
        state.add(id);
        state.remove(id);
        assertThrows(ServerNotTrackedException.class, () -> {
            state.getById(id);
        });
    }

    @Test
    void removeByNonexistentIdThrowsException() {
        long id = 123;
        assertThrows(ServerNotTrackedException.class, () -> {
            state.remove(id);
        });
    }

    // TODO test for removing server with count > 1
}
