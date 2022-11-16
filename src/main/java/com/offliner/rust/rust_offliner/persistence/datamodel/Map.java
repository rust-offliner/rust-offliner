package com.offliner.rust.rust_offliner.persistence.datamodel;

import javax.persistence.*;
import java.io.File;
import java.util.List;

@Entity
@Table(name = "Maps")
public class Map {

    @Id
    @Column(name = "map_id")
    private long id;

    @Column(name = "map_size")
    private int size;

    @Column(name = "image_path")
    private String imagePath;


    @OneToOne
    @JoinColumn(name = "server_id", referencedColumnName = "server_id")
    private Server server;

    @OneToMany(mappedBy = "map")
    private List<Base> baseList;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Map(long id, int size, String imagePath, List<Base> baseList) {
        this.id = id;
        this.size = size;
        this.imagePath = imagePath;
        this.baseList = baseList;
    }

    public Map() {

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
}
