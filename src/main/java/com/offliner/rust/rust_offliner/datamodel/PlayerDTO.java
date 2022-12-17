package com.offliner.rust.rust_offliner.datamodel;

import com.offliner.rust.rust_offliner.persistence.datamodel.BaseEntity;

import java.time.LocalDateTime;

public class PlayerDTO {

    private String name;
    private LocalDateTime lastSeen;
    private BaseEntity base;
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

    public BaseEntity getBase() {
        return base;
    }

    public void setBase(BaseEntity base) {
        this.base = base;
    }
}
