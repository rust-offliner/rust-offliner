package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import com.offliner.rust.rust_offliner.interfaces.IDataAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class ServerDataAccessObject implements IDataAccessObject<ServerEntity> {

    @Autowired
    JdbcTemplate jdbcTemplate;


    // TODO
    // add server query addition implementation

    @Override
    public boolean add(ServerEntity server) {
        jdbcTemplate.update("INSERT INTO `Server` VALUES (?, ?, ?, ?)", server.getServerId(), server.getIPAddress(), server.getPort(), server.getWipeDate());
        return false;
    }

    @Override
    public ServerEntity update(ServerEntity server) {
        return null;
    }

    @Override
    public boolean delete(ServerEntity server) {
        return false;
    }

    @Override
    public Optional<ServerEntity> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<ServerEntity> getAll() {
        return null;
    }
}
