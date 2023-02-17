package com.offliner.rust.rust_offliner.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
// stands for Count, Order, Date
public class StateCODWrapper {

    private List<Long> order = Collections.synchronizedList(new ArrayList<>());
    private List<Integer> count = Collections.synchronizedList(new ArrayList<>());
    private List<Instant> lastRequestDate = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    @Qualifier("lock")
    Object lock;

    public void increment(long id) {
        int index;
        synchronized (lock) {
            index = order.indexOf(id);
            int currentSubscribers = count.get(index);
//                    eachServerSubscribersCount.replace(id, currentSubscribers + 1);
            count.set(index, currentSubscribers + 1);
            lastRequestDate.set(index, Instant.now());
        }
    }

    public void add(long id) {
        synchronized (lock) {
            order.add(id);
            count.add(1);
            lastRequestDate.add(Instant.now());
        }
    }

    public boolean decrement(long id) {
        synchronized (lock) {
            int index = order.indexOf(id);
            if (count.get(index) == 1) {
//                    eachServerSubscribersCount.remove(id);
                remove(id, index);
                return true;
            }
//                int currentSubscribers = eachServerSubscribersCount.get(id);
//                eachServerSubscribersCount.replace(id, currentSubscribers - 1);
            // decrement subscribers
            int currentSubscribers = count.get(index);
            count.set(index, currentSubscribers - 1);
            lastRequestDate.set(index, Instant.now());
            return false;
        }
    }

    private void remove(long id, int index) {
        synchronized (lock) {
            count.remove(index);
            order.remove(id);
            lastRequestDate.remove(index);
        }
    }

    public List<Long> getOrder() {
        return order;
    }

    public List<Instant> getLastRequestDate() {
        return lastRequestDate;
    }
}
