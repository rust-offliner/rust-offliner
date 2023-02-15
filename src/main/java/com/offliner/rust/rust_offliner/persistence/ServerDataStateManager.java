package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.services.TrackingServersService;
import com.offliner.rust.rust_offliner.state.TrackingState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ServerDataStateManager {

//    private Map<Long, BattlemetricsServerDTO> state = Collections.synchronizedMap(new HashMap<>());
    private static final Logger log = LoggerFactory.getLogger(ServerDataStateManager.class);

    @Autowired
    IServerDao serverDao;

    @Autowired
    TrackingState state;

    @PostConstruct
    public void populate() {
        List<Integer> a = serverDao.getAllCurrentlyTrackedServerIds();
        for (int b:a) {
            log.info(String.valueOf(b));
        }
    }

//    @Autowired
//    AtomicLong counter;

//    public boolean add(long id, BattlemetricsServerDTO server) throws KeyAlreadyExistsException {
//        if (state.containsKey(id)) {
//            throw new KeyAlreadyExistsException("The key you are trying to put already exists");
//        }
//        serverDao.updateTrackedState(id, true);
//        state.put(id, server);
//        return true;
//    }
//
//    public BattlemetricsServerDTO get(long id) throws ServerNotTrackedException {
//        if (!state.containsKey(id)) {
//            throw new ServerNotTrackedException("Server " + id + " is not tracked");
//        }
//        return state.get(id);
//    }

//    public
//
    public boolean replace(long id, BattlemetricsServerDTO server) throws ServerNotTrackedException {
        state.replace(id, server);
        return true;
    }
//
//    /**
//     *
//     * @param id
//     *
//     * @throws ServerNotTrackedException
//     * server is not tracked meaning we can't remove it
//     *
//     * counter is decremented because when we remove one server, we automatically shrink our tracked list inside
//     * @see TrackingServersService#track()
//     */
//    public boolean remove(long id) throws ServerNotTrackedException {
//        if (!state.containsKey(id)) {
//            throw new ServerNotTrackedException("Server " + id + " is not tracked");
//        }
//        counter.decrementAndGet();
//        return true;
//    }
}
