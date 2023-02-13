package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.services.TrackingServersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ServerDataStateManager {

    private Map<Long, BattlemetricsServerDTO> state = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    IServerDao serverDao;

    @Autowired
    AtomicLong counter;

    public boolean add(long id, BattlemetricsServerDTO server) throws KeyAlreadyExistsException {
        if (state.containsKey(id)) {
            throw new KeyAlreadyExistsException("The key you are trying to put already exists");
        }
        serverDao.updateTrackedState(id, true);
        state.put(id, server);
        return true;
    }

    public BattlemetricsServerDTO get(long id) throws ServerNotTrackedException {
        if (!state.containsKey(id)) {
            throw new ServerNotTrackedException("Server " + id + " is not tracked");
        }
        return state.get(id);
    }

    public boolean replace(long id, BattlemetricsServerDTO server) throws ServerNotTrackedException {
        if (!state.containsKey(id)) {
            throw new ServerNotTrackedException("Server " + id + " is not tracked");
        }
        //TODO replace   // done?
        state.replace(id, server);
        return true;
    }

    /**
     *
     * @param id
     *
     * @throws ServerNotTrackedException
     * server is not tracked meaning we can't remove it
     *
     * counter is decremented because when we remove one server, we automatically shrink our tracked list inside
     * @see TrackingServersService#track()
     */
    public boolean remove(long id) throws ServerNotTrackedException {
        if (!state.containsKey(id)) {
            throw new ServerNotTrackedException("Server " + id + " is not tracked");
        }
        counter.decrementAndGet();
        return true;
    }
}
