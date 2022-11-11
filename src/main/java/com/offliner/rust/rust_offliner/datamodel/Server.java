package com.offliner.rust.rust_offliner.datamodel;

import java.util.Date;
import java.util.List;

public class Server {

    private final long serverId;
    private Date wipeDate;
    private Map map;
    private final String IPAddress;
    private final int port;
    private int playersCount;
    private List<Player> playersList;

    public Server(long serverId, Date wipeDate, Map map, String IPAddress, int port, List<Player> playersList) {
        this.serverId = serverId;
        this.wipeDate = wipeDate;
        this.map = map;
        this.IPAddress = IPAddress;
        this.port = port;
        this.playersList = playersList;

        this.playersCount = this.playersList.size();
    }

    public long getSERVERID() {
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
        return IPAddress;
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

    public List<Player> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(List<Player> playersList) {
        this.playersList = playersList;
    }
}
