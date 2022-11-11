package com.offliner.rust.rust_offliner.datamodel;

import java.util.Date;

public class Player {

    private final long STEAMID64;
    private final long BMID;
    private Date lastSeen;
    private boolean isOnline = false;
    private Base base;

    public Player(long STEAMID64, long BMID, Date lastSeen, Base base) {
        this.STEAMID64 = STEAMID64;
        this.BMID = BMID;
        this.lastSeen = lastSeen;
        this.base = base;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public long getSTEAMID64() {
        return STEAMID64;
    }

    public long getBMID() {
        return BMID;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public Base getBase() {
        return base;
    }
}
