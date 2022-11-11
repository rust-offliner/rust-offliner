package com.offliner.rust.rust_offliner.datamodel;

import java.util.List;

public class Base {

    private final int coordX;
    private final int coordY;
    private List<Player> playerList;
    private boolean isOnline;

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Base(int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }
}
