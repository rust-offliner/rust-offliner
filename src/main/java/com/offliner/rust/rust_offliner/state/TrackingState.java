package com.offliner.rust.rust_offliner.state;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.services.TrackingServersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;

@Component
public class TrackingState {

    private static final Logger log = LoggerFactory.getLogger(TrackingState.class);

    private List<TrackableServer> list = Collections.synchronizedList(new ArrayList<>());

    private int order = 0;

//    private static TrackingState instance = null;


    // we need an additional map to keep track of how many users follow each server (and also order for tracking)
//    private Map<Long, Integer> eachServerSubscribersCount = Collections.synchronizedMap(new LinkedHashMap<>());
    // list of ids
//    private List<Long> order = Collections.synchronizedList(new ArrayList<>());
    // count how many subscribers per each server (id) (in the same order as list above)
//    private List<Integer> count = Collections.synchronizedList(new ArrayList<>());

//    @Autowired
//    @Qualifier("lock")
    private final Object lock;

    public TrackingState(@Qualifier("lock") Object lock) {
        this.lock = lock;
    }

    //    @Autowired
//    StateCODWrapper cod;


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

//    public List<Long> getOrder() {
//        return cod.getOrder();
//    }

    public EServerDto add(long id) throws KeyAlreadyExistsException {
        log.debug("rozpoczynamy dodawnaie do listy");
        Long idWrapper = id;
        TrackableServer server = new TrackableServer(id, new EServerDto(id), 1, Instant.now());
        synchronized (lock) {
//            if (state.containsKey(id)) {
//                throw new KeyAlreadyExistsException("The key you are trying to put already exists");
//            }
//            // we don't have state of this server yet
//            state.put(id, null);
//
//            cod.add(id);
            if (contains(idWrapper)) {
                throw new KeyAlreadyExistsException("The key you are trying to put already exists");
            }
            list.add(server);
            log.debug("item has been added to list");
        }
        return server.getData();
    }

    public void add(long id, EServerDto serverDto) throws KeyAlreadyExistsException {
        Long idWrapper = id;
        synchronized (lock) {
            if (contains(idWrapper)) {
                TrackableServer server = list.get(indexOf(idWrapper));
//                TrackableServer replace = new TrackableServer(id);
//                replace.setData(server);
//                replace.setCount();
                server.setCount(server.getCount() + 1);
                server.setLastRequested(Instant.now());
                throw new KeyAlreadyExistsException("The key you are trying to put already exists");
            }
            list.add(new TrackableServer(id, serverDto, 1, Instant.now()));
//                if (state.containsKey(id)) {
//                    cod.increment(id);
////                    int index = order.indexOf(id);
////                    int currentSubscribers = count.get(index);
////                    eachServerSubscribersCount.replace(id, currentSubscribers + 1);
////                    count.set(index, currentSubscribers + 1);
//                    throw new KeyAlreadyExistsException("The key you are trying to put already exists");
//                }
//                state.put(id, server);
////        order.add(id);
////            order.add(id);
////            count.add(1);
//            cod.add(id);
//                eachServerSubscribersCount.put(id, 1);
//        mapSize++;

        }

    }

    public TrackableServer getById(long id) throws ServerNotTrackedException {
        Long idWrapper = id;
        synchronized (lock) {
            if (!contains(idWrapper)) {
                log.debug(idWrapper + " to nasz idWrapper, kktorego nie ma w liscie?");
                throw new ServerNotTrackedException("CHUUUUUUUUUJServer " + id + " is not tracked");
            }
            TrackableServer server = list.get(indexOf(idWrapper));
            server.setLastRequested(Instant.now());
            return server;
        }
    }

    public EServerDto getByPosition(int index) {
        synchronized (lock) {
            if (index > list.size()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            TrackableServer server = list.get(index);
            server.setLastRequested(Instant.now());
            return server.getData();
        }
    }

    public void replace(long id, EServerDto serverDto) throws ServerNotTrackedException {
        Long idWrapper = id;
        log.debug("id w replace " + idWrapper);
        synchronized (lock) {
            if (!contains(idWrapper)) {
                throw new ServerNotTrackedException("Server " + id + " is not tracked");
            }
//            int position = ;
            TrackableServer server = list.get(indexOf(idWrapper));
            log.debug("dane serwera " + server);
            server.setData(serverDto);
            server.setLastRequested(Instant.now());
//            server.setCount(server.getCount() + 1);
//            state.replace(id, server);
//            list.set()
        }
    }

    /**
     * @param id identifier
     * @return true on last item (need to update DB)
     * false otherwise
     * @throws ServerNotTrackedException server is not tracked meaning we can't remove it
     * @see TrackingServersService#track()
     * counter is decremented because when we remove one server, we automatically shrink our tracked list inside
     */
    public boolean remove(long id) throws ServerNotTrackedException {
        Long idWrapper = id;
        synchronized (lock) {
                if (!contains(idWrapper)) {
                    throw new ServerNotTrackedException("Server " + id + " is not tracked");
                }
//        index.decrementAndGet();
//                if (eachServerSubscribersCount.get(id) == 1) {
//            mapSize--;
            // if there are more followers than just this one we are now removing
            int index = indexOf(idWrapper);
            // we move order one to the left to prevent "skipping" one server within our tracking class
            if (index < order % list.size())
                order--;
            if (list.get(index).getCount() > 1) {
                TrackableServer server = list.get(index);
                server.setLastRequested(Instant.now());
                server.decrement();
            } else {
                list.remove(index);
                return true;
            }
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

        return false;
    }

    public int getOrder() {
        synchronized (lock) {
            return order++ % list.size();
        }
    }

    public void setOrder(int order) {
        synchronized (lock) {
            this.order = order;
        }
    }

    public boolean contains(long id) {
        Long idWrapper = id;
        synchronized (lock) {
            // unfortunately #list.contains() doesn't work because of Java's internal implementation
            for (TrackableServer server : list) {
                if (server.equals(idWrapper))
                    return true;
            }
            return false;
//            return list.contains(id);
        }
    }

    public int indexOf(long id) {
        int i = 0;
        synchronized (lock) {
            for (TrackableServer server : list) {
                if (server.getId() == id)
                    return i;
                i++;
            }
        }
        return -1;
    }

    public Iterator<TrackableServer> getIterator() {
        synchronized (lock) {
            return list.iterator();
        }
    }


    //    public List<Instant> getLastServerFetchDate() {
//        return cod.getLastRequestDate();
//    }
}
