package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "base")
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "base_id")
    private long id;
    private int coordX;
    private int coordY;

    @OneToMany(mappedBy = "base")
    private List<Player> playerList;
    private boolean isOnline;


//    @OneToMany()
//    private Player player;

    @ManyToOne
    @JoinColumn(name = "map_id")
    private Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Base() {

    }

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

    public Base(long id, int coordX, int coordY) {
        this.id = id;
        this.coordX = coordX;
        this.coordY = coordY;
    }
}
