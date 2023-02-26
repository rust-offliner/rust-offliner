package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.state.TrackingState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class ServerDataStateManagerTest {

    private IServerDao dao;

    private final ServerDataStateManager manager;

    private final TrackingState state;

    @Autowired
    public ServerDataStateManagerTest(IServerDao dao, ServerDataStateManager manager, TrackingState state) {
        this.dao = dao;
        this.manager = manager;
        this.state = state;
    }

    @BeforeEach
    void checkInit() {
        // all servers are untracked on init
        List<Integer> list =  dao.getAllByTrackedIsTrue();
        assertEquals(list.size(), 0);
    }

    @Test
    void manageIncrementsCountOnExistingId() throws ServerNotTrackedException, KeyAlreadyExistsException {
        long id = 123;
        EServerDto serverDto = new EServerDto(id);
        manager.manage(id, serverDto);
        assertEquals(state.getById(id).getCount(), 1);
        manager.manage(id, serverDto);
        assertEquals(state.getById(id).getCount(), 2);
    }

    @Test
    void managerAddsToState() throws ServerNotTrackedException, KeyAlreadyExistsException {
        long id = 1234;
        EServerDto serverDto = new EServerDto(id);
        manager.manage(id, serverDto);
        assertEquals(state.getById(id).getCount(), 1);
    }

}
