package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "base")
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "base_id")
    private long id;
    private int coordX;
    private int coordY;

    @OneToMany(mappedBy = "base")
    private List<PlayerEntity> playerList;
    private boolean isOnline;


//    @OneToMany()
//    private Player player;

    @ManyToOne
    @JoinColumn(name = "map_id")
    private MapEntity map;

    public MapEntity getMap() {
        return map;
    }

    public void setMap(MapEntity map) {
        this.map = map;
    }

    public BaseEntity() {

    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public List<PlayerEntity> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerEntity> playerList) {
        this.playerList = playerList;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public BaseEntity(long id, int coordX, int coordY) {
        this.id = id;
        this.coordX = coordX;
        this.coordY = coordY;
    }
}
