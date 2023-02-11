package com.offliner.rust.rust_offliner.services;

import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackingServersService {

    private static final Logger log = LoggerFactory.getLogger(TrackingServersService.class);
    @Autowired
    IServerDao serverDao;

    @Value("${battlemetrics.tracked.max}")
    private int maxTrackedServers;
    public void track() {
        List<Long> tracked = serverDao.getAllByServerIdAndCurrentlyTrackedIsTrue();
        for (Long i : tracked)
            log.info(i.toString());
    }

    public long getDelay() {
        long currentlyTrackedServersCount = serverDao.countAllByCurrentlyTrackedIsTrue();
        log.info(currentlyTrackedServersCount + " tracked servers");
//        int delay = (int)(currentlyTrackedServersCount / maxTrackedServers) * 60000 + 60000; // every minute for <60 tracked servers, 2 minutes for <120 servers etc
        int delay = 3000;
        return delay;
    }
}
