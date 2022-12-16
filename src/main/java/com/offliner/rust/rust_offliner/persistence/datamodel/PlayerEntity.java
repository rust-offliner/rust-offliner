package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @Column(name = "battlemetrics_id")
    private long BMID;

    @Column(name = "name")
    private String name;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Transient
    private boolean isOnline = false;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    @ManyToOne
    @JoinColumn(name = "base_id")
    private BaseEntity base;

    @Column(name = "followed")
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

    public BaseEntity getBase() {
        return base;
    }

    public void setBase(BaseEntity base) {
        this.base = base;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    public PlayerEntity(long BMID, LocalDateTime lastSeen) {
        this.BMID = BMID;
        this.lastSeen = lastSeen;
    }

    public PlayerEntity(long BMID, String name, LocalDateTime lastSeen) {
        this.BMID = BMID;
        this.name = name;
        this.lastSeen = lastSeen;
    }

    public PlayerEntity() {

    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }


    public long getBMID() {
        return BMID;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

}
