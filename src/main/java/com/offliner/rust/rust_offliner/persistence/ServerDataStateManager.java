package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.state.TrackingState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServerDataStateManager {

//    private Map<Long, BattlemetricsServerDTO> state = Collections.synchronizedMap(new HashMap<>());
    private static final Logger log = LoggerFactory.getLogger(ServerDataStateManager.class);

    int i = 0;
//    @Autowired
    IServerDao serverDao;

//
//    @Autowired
//    @Qualifier("state")
    TrackingState state;
//
//    @Autowired
    Object lock;

    public ServerDataStateManager(IServerDao serverDao, TrackingState state, @Qualifier("lock") Object lock) {
        this.serverDao = serverDao;
        this.state = state;
        this.lock = lock;
    }

    @PostConstruct
    @Transactional
    public void populate() {
//        List<Integer> a = serverDao.getAllCurrentlyTrackedServerIds();
//        for (int b:a) {
//            log.info(String.valueOf(b));
//        }
        serverDao.initTrackedState();
    }

    public long getCurrentlyTrackedServer() {
        List<Long> list;
        // copy list to prevent excessively long usage of synchronized resource
        synchronized (lock) {
            log.info(state.getOrder().size() + "state");
            list = new ArrayList<>(state.getOrder());
        }
        try {
            log.info(String.valueOf(list.size()));
            return list.get(i++);
        } catch (IndexOutOfBoundsException e) {
            log.warn("State index out of bounds");
            i = 0;
            return list.get(i++);
        }
    }

    public void manage(long id, BattlemetricsServerDTO server) throws KeyAlreadyExistsException, ServerNotTrackedException {
        if (state.getOrder().contains(id)) {
            state.replace(id, server);
        } else {
            state.add(id, server);
        }
    }

//    @Autowired
//    AtomicLong counter;

    public void add(long id) throws KeyAlreadyExistsException {
        state.add(id);
        if (serverDao.existsByServerId(id)) {
            serverDao.updateTrackedState(id, true);
        }
//        serverDao.save();
    }
//
    public BattlemetricsServerDTO get(long id) throws ServerNotTrackedException {
        return state.get(id);
    }

//    public
//
//    @Override
//    public boolean replace(long id, BattlemetricsServerDTO server) throws ServerNotTrackedException {
//        super.replace(id, server);
//        return true;
//    }
    @Transactional
    public void unsubscribe(long id) throws ServerNotTrackedException {
        boolean shouldRemoveFromDB = state.remove(id);
        if (shouldRemoveFromDB) {
            serverDao.updateTrackedState(id, false);
        }

    }

    public List<Instant> getLastServerFetchDate() {
        return state.getLastServerFetchDate();
    }
}
