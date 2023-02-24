package com.offliner.rust.rust_offliner.config;

import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.persistence.ServerDataStateManager;
import com.offliner.rust.rust_offliner.state.TrackableServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Iterator;

@Component
public class UnusedServersRemovalScheduler {

    private static final Logger log = LoggerFactory.getLogger(UnusedServersRemovalScheduler.class);

//    @Autowired
    final IServerDao serverDao;

//    @Autowired
//    @Qualifier("lock")
    final Object lock;

//    @Autowired
//    TrackingState state;

//    @Autowired
    final ServerDataStateManager manager;

    @Autowired
    public UnusedServersRemovalScheduler(@Qualifier("lock") Object lock, IServerDao serverDao, ServerDataStateManager manager) {
        this.manager = manager;
        this.lock = lock;
        this.serverDao = serverDao;
    }

    @Scheduled(cron = "15,45 * * * * ?")
    @Transactional
    @ConditionalOnProperty(name = "schedular.removal.enabled")
    public void remove() {
        Iterator<TrackableServer> iterator;
        Instant now = Instant.now();
        int delay = 5 * 60000; // how much of a delay for us to consider server untracked
        synchronized (lock) {
            iterator = manager.getIterator();

        while (iterator.hasNext()) {
            TrackableServer server = iterator.next();
            if (now.compareTo(server.getLastRequested().plusMillis(delay)) > 0) {
                manager.removeFromDB(server.getId());
                iterator.remove();
            }
        }
//        for (TrackableServer server : iterator.) {
//            // last update was earlier than the past 5 minutes window
//            if (now.compareTo(date.plusMillis(delay)) > 0) {
                // call to a method in serever manager to remove
//                manager.unsubscribe(); //TODO

        }
    }

}
