package com.offliner.rust.rust_offliner.state;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.services.TrackingServersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TrackingState {

    private static final Logger log = LoggerFactory.getLogger(TrackingState.class);

    private static TrackingState instance = null;

    private Map<Long, BattlemetricsServerDTO> state = Collections.synchronizedMap(new HashMap<>());

    private AtomicLong index;

    // it's faster to manually track size of the map instead of calling state.size()
    private int mapSize;


    // we don't want multiple state instances, just one
    private TrackingState() {
        index.set(0);
        log.info("State instantiated");
    }

    public static TrackingState getInstance() {
        if (instance == null)
            instance = new TrackingState();
        return instance;
    }

    public void add(long id, BattlemetricsServerDTO server) throws KeyAlreadyExistsException {
        if (state.containsKey(id)) {
            throw new KeyAlreadyExistsException("The key you are trying to put already exists");
        }
        state.put(id, server);
    }

    public BattlemetricsServerDTO get(long id) throws ServerNotTrackedException {
        if (!state.containsKey(id)) {
            throw new ServerNotTrackedException("Server " + id + " is not tracked");
        }
        return state.get(id);
    }

    public void replace(long id, BattlemetricsServerDTO server) throws ServerNotTrackedException {
        if (!state.containsKey(id)) {
            throw new ServerNotTrackedException("Server " + id + " is not tracked");
        }
        state.replace(id, server);
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
        index.decrementAndGet();
        return true;
    }
}
