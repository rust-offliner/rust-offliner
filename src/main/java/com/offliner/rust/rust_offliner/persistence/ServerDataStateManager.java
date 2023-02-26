package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;
import com.offliner.rust.rust_offliner.datamodel.converters.ServerDTOConverter;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.interfaces.IUserDao;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.UserEntity;
import com.offliner.rust.rust_offliner.state.TrackableServer;
import com.offliner.rust.rust_offliner.state.TrackingState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Optional;

@Service
public class ServerDataStateManager {
    private static final Logger log = LoggerFactory.getLogger(ServerDataStateManager.class);

//    int i = 0;
//    @Autowired
    IServerDao serverDao;

    IUserDao userDao;

    ServerDTOConverter converter;

//
//    @Autowired
//    @Qualifier("state")
    TrackingState state;
//
//    @Autowired
    Object lock;

    public ServerDataStateManager(IServerDao serverDao, IUserDao userDao, TrackingState state, @Qualifier("lock") Object lock, ServerDTOConverter converter) {
        this.serverDao = serverDao;
        this.state = state;
        this.userDao = userDao;
        this.lock = lock;
        this.converter = converter;
    }

    @PostConstruct
    @Transactional
    public void populate() {
//        List<Integer> a = serverDao.getAllCurrentlyTrackedServerIds();
//        for (int b:a) {
//            log.info(String.valueOf(b));
//        }
        serverDao.initTrackedState();
        serverDao.clearServersToUsersTable();
    }

    public long getCurrentlyTrackedServer() throws ServerNotTrackedException {
//        List<Long> list;
        // copy list to prevent excessively long usage of synchronized resource
//        synchronized (lock) {
//            log.info(state.getOrder().size() + "state");
//            list = new ArrayList<>(state.getOrder());
//        }
        int index = state.getOrder();
        log.debug(index + "index w getCurrentlyTrackedServer w managerze");
        try {
//            log.info(String.valueOf(list.size()));
            return state.getByPosition(index).getId();
        } catch (IndexOutOfBoundsException e) {
            // that should neven happen but just in case
            log.warn("State index out of bounds");
            state.setOrder(0);
            return state.getByPosition(state.getOrder()).getId();
        }
    }

    public void manage(long id, EServerDto server) throws KeyAlreadyExistsException, ServerNotTrackedException {
//        if (state.contains(id)) {
//            state.replace(id, server);
//        } else {
        state.add(id, server);
//        }
    }

//    @Autowired
//    AtomicLong counter;

    @Transactional
    public void add(long id) throws KeyAlreadyExistsException {
        log.debug("w managerze na poczattku");
        EServerDto server = state.add(id);
        log.debug("after adding to state");
        ServerEntity serverEntity = converter.convert(server);
        Optional<ServerEntity> help = serverDao.findByServerId(id);
        UserEntity user = getUserEntity();
        log.debug("user " + user.getUsername() + " started following server id " + id);

        // lazy load users who already track the server
        if (help.isPresent()) {
            help.get().getUsers().forEach(serverEntity::addUser);
            // user is already present as following this server
            if (!serverEntity.addUser(user))
                throw new KeyAlreadyExistsException("You can't follow a server you are already following!");
        } else {
            serverEntity.addUser(user);
        }
        serverEntity.setTracked(true);
        serverDao.save(serverEntity);
    }
//
    public EServerDto get(long id) throws ServerNotTrackedException {
        return state.getById(id).getData();
    }

//    public
//
//    @Override
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

    @Transactional
    public void removeFromDB(long id) {
        serverDao.updateTrackedState(id, false);
    }

    public Iterator<TrackableServer> getIterator() {
        return state.getIterator();
    }

    private UserEntity getUserEntity() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        return userDao.findByUsername(username);
    }
}
