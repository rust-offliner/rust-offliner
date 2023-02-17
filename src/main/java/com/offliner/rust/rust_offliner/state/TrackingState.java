package com.offliner.rust.rust_offliner.state;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.services.TrackingServersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;

@Component
public class TrackingState {

    private static final Logger log = LoggerFactory.getLogger(TrackingState.class);

//    private static TrackingState instance = null;

    private Map<Long, BattlemetricsServerDTO> state = Collections.synchronizedMap(new HashMap<>());

    // we need an additional map to keep track of how many users follow each server (and also order for tracking)
//    private Map<Long, Integer> eachServerSubscribersCount = Collections.synchronizedMap(new LinkedHashMap<>());
    // list of ids
//    private List<Long> order = Collections.synchronizedList(new ArrayList<>());
    // count how many subscribers per each server (id) (in the same order as list above)
//    private List<Integer> count = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    @Qualifier("lock")
    Object lock;

    @Autowired
    StateCODWrapper cod;

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

    public List<Long> getOrder() {
        return cod.getOrder();
    }

    public void add(long id) throws KeyAlreadyExistsException {
        synchronized (lock) {
            if (state.containsKey(id)) {
                throw new KeyAlreadyExistsException("The key you are trying to put already exists");
            }
            // we don't have state of this server yet
            state.put(id, null);

            cod.add(id);
        }
    }

    public void add(long id, BattlemetricsServerDTO server) throws KeyAlreadyExistsException {
        synchronized (lock) {
                if (state.containsKey(id)) {
                    cod.increment(id);
//                    int index = order.indexOf(id);
//                    int currentSubscribers = count.get(index);
//                    eachServerSubscribersCount.replace(id, currentSubscribers + 1);
//                    count.set(index, currentSubscribers + 1);
                    throw new KeyAlreadyExistsException("The key you are trying to put already exists");
                }
                state.put(id, server);
//        order.add(id);
//            order.add(id);
//            count.add(1);
            cod.add(id);
//                eachServerSubscribersCount.put(id, 1);
//        mapSize++;

        }

    }

    public BattlemetricsServerDTO get(long id) throws ServerNotTrackedException {
        synchronized (lock) {
            if (!state.containsKey(id)) {
                throw new ServerNotTrackedException("Server " + id + " is not tracked");
            }
            return state.get(id);
        }
    }

    public void replace(long id, BattlemetricsServerDTO server) throws ServerNotTrackedException {
        synchronized (lock) {
            if (!state.containsKey(id)) {
                throw new ServerNotTrackedException("Server " + id + " is not tracked");
            }
            state.replace(id, server);
        }
    }

    /**
     *
     * @param id
     * identifier
     *
     * @return
     * true on last item (need to update DB)
     * false otherwise
     *
     * @throws ServerNotTrackedException
     * server is not tracked meaning we can't remove it
     *
     * @see TrackingServersService#track()
     * counter is decremented because when we remove one server, we automatically shrink our tracked list inside
     */
    public boolean remove(long id) throws ServerNotTrackedException {
        synchronized (lock) {
                if (!state.containsKey(id)) {
                    throw new ServerNotTrackedException("Server " + id + " is not tracked");
                }
//        index.decrementAndGet();
//                if (eachServerSubscribersCount.get(id) == 1) {
//            mapSize--;
            if (cod.decrement(id)) {
                state.remove(id);
                return true;
            }
            return false;
//            int index = order.indexOf(id);
//            if (count.get(index) == 1) {
//                state.remove(id);
////                    eachServerSubscribersCount.remove(id);
//                count.remove(index);
//                order.remove(id);
//                return true;
//            }
////                int currentSubscribers = eachServerSubscribersCount.get(id);
////                eachServerSubscribersCount.replace(id, currentSubscribers - 1);
//            // decrement subscribers
//            int currentSubscribers = count.get(index);
//            count.set(index, currentSubscribers - 1);
//            return false;
//        order.remove(id);
            }

    }

    public List<Instant> getLastServerFetchDate() {
        return cod.getLastRequestDate();
    }
}
