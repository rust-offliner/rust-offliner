package com.offliner.rust.rust_offliner.datamodel;

import java.time.LocalDateTime;
import java.util.Date;

public class Player {

//    private final long STEAMID64;
    private final long BMID;

    private String name;
    private LocalDateTime lastSeen;
    private boolean isOnline = false;
    private Base base;

    public Player(long BMID, LocalDateTime lastSeen) {
//        this.STEAMID64 = STEAMID64;
        this.BMID = BMID;
        this.lastSeen = lastSeen;
    }

    public Player(long BMID, String name, LocalDateTime lastSeen) {
        this.BMID = BMID;
        this.name = name;
        this.lastSeen = lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setBase(Base base) {
        this.base = base;
    }

//    public long getSTEAMID64() {
//        return STEAMID64;
//    }

    public long getBMID() {
        return BMID;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public Base getBase() {
        return base;
    }
}
