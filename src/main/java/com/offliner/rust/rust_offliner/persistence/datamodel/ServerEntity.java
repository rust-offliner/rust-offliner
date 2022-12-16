package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "server")
public class ServerEntity {

    @Id
    @Column(name = "server_id")
    private long serverId;

    @Column(name = "wipe_date")
    private Date wipeDate;

    @OneToOne
    @JoinColumn(name = "server_id", referencedColumnName = "server_id")
    private MapEntity map;

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
    private Set<UserEntity> users;

    @Transient
    private int playersCount;

    @OneToMany(mappedBy = "server")
    private List<PlayerEntity> followedPlayersList;

    public ServerEntity(long serverId, Date wipeDate, MapEntity map, String addressIp, int port, List<PlayerEntity> playersList) {
        this.serverId = serverId;
        this.wipeDate = wipeDate;
        this.map = map;
        this.addressIp = addressIp;
        this.port = port;
        this.followedPlayersList = playersList;

        this.playersCount = this.followedPlayersList.size();
    }

    public ServerEntity(long serverId, String addressIp, int port) {
        this.serverId = serverId;
        this.addressIp = addressIp;
        this.port = port;
    }

    public ServerEntity() {

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

    public MapEntity getMap() {
        return map;
    }

    public void setMap(MapEntity map) {
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

    public List<PlayerEntity> getFollowedPlayersList() {
        return followedPlayersList;
    }

    public void setFollowedPlayersList(List<PlayerEntity> followedPlayersList) {
        this.followedPlayersList = followedPlayersList;
    }
}
