package com.offliner.rust.rust_offliner.services.service_datamodel.ServerTemplate;

import com.offliner.rust.rust_offliner.datamodel.Player;

import java.util.List;

public class ServerTemplate {

    private final long id;
    private final String name;
    private int currentPlayers;
    private int maxPlayers;

    private List<Player> playerList;

    public ServerTemplate(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
