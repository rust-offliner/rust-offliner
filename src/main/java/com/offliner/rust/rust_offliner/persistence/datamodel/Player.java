package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Players")
public class Player {

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
    private Server server;

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Base base;


    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Player(long BMID, LocalDateTime lastSeen) {
        this.BMID = BMID;
        this.lastSeen = lastSeen;
    }

    public Player(long BMID, String name, LocalDateTime lastSeen) {
        this.BMID = BMID;
        this.name = name;
        this.lastSeen = lastSeen;
    }

    public Player() {

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
