package com.offliner.rust.rust_offliner.state;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.services.TrackingServersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TrackingState {

    private static final Logger log = LoggerFactory.getLogger(TrackingState.class);

//    private static TrackingState instance = null;

    private Map<Long, BattlemetricsServerDTO> state = Collections.synchronizedMap(new HashMap<>());

    // we need an additional map to keep track of how many users follow each server (and also order for tracking)
    private Map<Long, Integer> eachServerSubscribersCount = Collections.synchronizedMap(new LinkedHashMap<>());

//    private AtomicLong index;

    // it's faster to manually track size of the map (and list) instead of calling state.size()
//    private int mapSize;


    // we don't want multiple state instances, just one
//    private TrackingState() {
//        index.set(0);
//        mapSize = 0;
//        log.info("State instantiated");
//    }
//
//    public static TrackingState getInstance() {
//        if (instance == null)
//            instance = new TrackingState();
//        return instance;
//    }
    @PostConstruct
    private void init() {
//        index = new AtomicLong();
//        mapSize = 0;
        log.info("State instantiated");
    }

    public void add(long id, BattlemetricsServerDTO server) throws KeyAlreadyExistsException {
        if (state.containsKey(id)) {
            int currentSubscribers = eachServerSubscribersCount.get(id);
            eachServerSubscribersCount.replace(id, currentSubscribers + 1);
            throw new KeyAlreadyExistsException("The key you are trying to put already exists");
        }
        state.put(id, server);
//        order.add(id);
        eachServerSubscribersCount.put(id, 1);
//        mapSize++;
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
    public void remove(long id) throws ServerNotTrackedException {
        if (!state.containsKey(id)) {
            throw new ServerNotTrackedException("Server " + id + " is not tracked");
        }
//        index.decrementAndGet();
        if (eachServerSubscribersCount.get(id) == 1) {
//            mapSize--;
            state.remove(id);
            eachServerSubscribersCount.remove(id);
            return;
        }
        int currentSubscribers = eachServerSubscribersCount.get(id);
        eachServerSubscribersCount.replace(id, currentSubscribers - 1);
//        order.remove(id);
    }
}
