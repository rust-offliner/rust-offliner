package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "server")
public class ServerEntity {

    @Id
    @Column(name = "server_id")
    private long serverId;

    @Column(name = "wipe_date")
    private LocalDateTime wipeDate;

    @OneToOne
    @JoinColumn(name = "server_id", referencedColumnName = "server_id")
    private MapEntity map;

    @Column(name = "ip_address")
    private String addressIp;

    @Column(name = "port")
    private int port;

    @Column(name = "tracked")
    private boolean tracked;

    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JoinTable(
            name = "servers_to_users",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> users = new HashSet<>();

    @Transient
    private int playersCount;

    @OneToMany(mappedBy = "server")
    private List<PlayerEntity> followedPlayersList;

    public ServerEntity(long serverId, LocalDateTime wipeDate, MapEntity map, String addressIp, int port, List<PlayerEntity> playersList) {
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

    public void addUser(UserEntity user) {
        this.users.add(user);
        user.getServers().add(this);
    }

    public void removeUser(UserEntity user) {
        this.users.remove(user);
        user.getServers().remove(this);
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    public LocalDateTime getWipeDate() {
        return wipeDate;
    }

    public void setWipeDate(LocalDateTime wipeDate) {
        this.wipeDate = wipeDate;
    }

    public MapEntity getMap() {
        return map;
    }

    public void setAddressIp(String addressIp) {
        this.addressIp = addressIp;
    }

    public void setPort(int port) {
        this.port = port;
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

    public boolean isTracked() {
        return tracked;
    }

    @Override
    public String toString() {
        return "ServerEntity{" +
                "serverId=" + serverId +
                ", wipeDate=" + wipeDate +
                ", map=" + map +
                ", addressIp='" + addressIp + '\'' +
                ", port=" + port +
                ", tracked=" + tracked +
//                ", users=" + users +
                ", playersCount=" + playersCount +
//                ", followedPlayersList=" + followedPlayersList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerEntity that = (ServerEntity) o;
        return serverId == that.serverId && port == that.port && tracked == that.tracked && Objects.equals(wipeDate, that.wipeDate) && Objects.equals(addressIp, that.addressIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, wipeDate, map, addressIp, port, tracked, users, followedPlayersList);
    }
}
