package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "maps")
public class MapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private long id;

    @Column(name = "map_size")
    private int size;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "time")
    private Instant time;

    @Column(name = "current")
    private boolean current;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    @OneToMany(mappedBy = "map")
    private List<BaseEntity> baseList;

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    public MapEntity(long id, int size, String imagePath, List<BaseEntity> baseList) {
        this.id = id;
        this.size = size;
        this.imagePath = imagePath;
        this.baseList = baseList;
    }

    public MapEntity() {

    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public File getImage() {
        return new File(this.imagePath);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setBaseList(List<BaseEntity> baseList) {
        this.baseList = baseList;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
