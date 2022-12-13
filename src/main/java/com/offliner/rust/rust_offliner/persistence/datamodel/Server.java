package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "server")
public class Server {

    @Id
    @Column(name = "server_id")
    private long serverId;

    @Column(name = "wipe_date")
    private Date wipeDate;

    @OneToOne
    @JoinColumn(name = "server_id", referencedColumnName = "server_id")
    private Map map;

    @Column(name = "ip_address")
    private String addressIp;

    @Column(name = "port")
    private int port;

    @ManyToMany
    @JoinTable(
            name = "servers_to_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "server_id")
    )
    private Set<User> users;

    @Transient
    private int playersCount;

    @OneToMany(mappedBy = "server")
    private List<Player> followedPlayersList;

    public Server(long serverId, Date wipeDate, Map map, String addressIp, int port, List<Player> playersList) {
        this.serverId = serverId;
        this.wipeDate = wipeDate;
        this.map = map;
        this.addressIp = addressIp;
        this.port = port;
        this.followedPlayersList = playersList;

        this.playersCount = this.followedPlayersList.size();
    }

    public Server(long serverId, String addressIp, int port) {
        this.serverId = serverId;
        this.addressIp = addressIp;
        this.port = port;
    }

    public Server() {

    }

    public long getServerId() {
        return serverId;
    }

    public Date getWipeDate() {
        return wipeDate;
    }

    public void setWipeDate(Date wipeDate) {
        this.wipeDate = wipeDate;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String getIPAddress() {
        return addressIp;
    }

    public int getPort() {
        return port;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public List<Player> getFollowedPlayersList() {
        return followedPlayersList;
    }

    public void setFollowedPlayersList(List<Player> followedPlayersList) {
        this.followedPlayersList = followedPlayersList;
    }
}
