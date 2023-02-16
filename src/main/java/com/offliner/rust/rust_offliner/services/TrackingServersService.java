package com.offliner.rust.rust_offliner.services;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.datamodel.converters.ServerDTOConverter;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import com.offliner.rust.rust_offliner.exceptions.ServerNotTrackedException;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.persistence.ServerDataStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackingServersService {

    private static final Logger log = LoggerFactory.getLogger(TrackingServersService.class);

    @Autowired
    IServerDao serverDao;

    @Autowired
    BattlemetricsServerService service;

    @Autowired
    ServerDataStateManager manager;

    @Autowired
    ServerDTOConverter converter;

    @Autowired
    @Qualifier("lock")
    Object lock;

    @Value("${battlemetrics.tracked.max}")
    private int maxTrackedServers;

//    @Autowired
//    private AtomicLong index;

//    @PostConstruct
//    private void init() {
//        index.set(0);
//    }

    // TODO insert tracked data into DB
    public void track() throws ServerNotTrackedException, KeyAlreadyExistsException {
        List<Integer> tracked = serverDao.getAllByTrackedIsTrue();
        int count = tracked.size();
        if (count == 0) {
            return;
        }
//        log.info("before");
        long currentlyTrackedId = manager.getCurrentlyTrackedServer();
//        log.info("after");
        BattlemetricsServerDTO server = service.getServer(currentlyTrackedId);

        serverDao.save(converter.convert(server));

        log.info(String.valueOf(server));
        synchronized (lock) {
            manager.manage(currentlyTrackedId, server);
        }

//        long pagingIndex = index.get(); // prevent mutating this field and breaking loop while updating state in other class
//        for (int i = (int) pagingIndex; i < pagingIndex + 10; ++i) {
//            BattlemetricsServerDTO server = service.getServer(tracked.get(i));
//            try {
//                manager.replace(tracked.get(i), server);
//            } catch (ServerNotTrackedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        index.addAndGet(10);
//            log.info(i.toString());
    }

    public long getDelay() {
        long count = serverDao.countAllByTrackedIsTrue(); // all tracked servers count
        log.info(count + " tracked servers");
        // prevent dividing by 0
        if (count == 0) {
            return 10000;
        }
//        int delay = (int)(currentlyTrackedServersCount / maxTrackedServers) * 60000 + 60000; // every minute for <60 tracked servers, 2 minutes for <120 servers etc.
//        if (count < 10) return 60000; // 60 seconds
//        if (count < 20) return 30000;
//        return (int) Math.ceil(600000.0 / count);
        if (count > 60) return 1000;
        return 60000 / count;
//        int delay = 3000;
//        return delay;
    }
}
