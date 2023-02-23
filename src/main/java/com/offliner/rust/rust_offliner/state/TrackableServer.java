package com.offliner.rust.rust_offliner.state;

import com.offliner.rust.rust_offliner.datamodel.EServerDto;

import java.time.Instant;

public class TrackableServer {

    private EServerDto data;
    private long id;
//    int order;
    private int count;

    private Instant lastRequested;

    public TrackableServer(long id, EServerDto data, int count, Instant instant) {
        this.data = data;
        this.id = id;
//        this.order = order;
        this.count = count;
        this.lastRequested = instant;
    }

    public TrackableServer() {
        this.data = null;
        this.id = 0;
        this.count = 0;
    }

    public TrackableServer(long id) {
        this.id = id;
        this.data = null;
        this.count = 0;
    }

    public void decrement() {
        this.count--;
    }

    public EServerDto getData() {
        return data;
    }

    public long getId() {
        return id;
    }

//    public int getOrder() {
//        return order;
//    }

    public int getCount() {
        return count;
    }

    public void setData(EServerDto data) {
        this.data = data;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Instant getLastRequested() {
        return lastRequested;
    }

    public void setLastRequested(Instant lastRequested) {
        this.lastRequested = lastRequested;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Long) {
            return (Long) obj == id;
        }
        if (obj instanceof EServerDto) {
            return (((EServerDto) obj).getId() == id && obj.equals(data));
        }
        return false;
    }

}
