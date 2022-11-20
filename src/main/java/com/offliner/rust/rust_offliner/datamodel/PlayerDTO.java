package com.offliner.rust.rust_offliner.datamodel;

import com.offliner.rust.rust_offliner.persistence.datamodel.Base;

import java.time.LocalDateTime;

public class PlayerDTO {

    private String name;
    private LocalDateTime lastSeen;
    private Base base;
    private boolean followed;

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }
}
