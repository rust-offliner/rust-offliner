package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.persistence.datamodel.Server;
import com.offliner.rust.rust_offliner.interfaces.IDataAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class ServerDataAccessObject implements IDataAccessObject<Server> {

    @Autowired
    JdbcTemplate jdbcTemplate;


    // TODO
    // add server query addition implementation

    @Override
    public boolean add(Server server) {
        jdbcTemplate.update("INSERT INTO `Server` VALUES (?, ?, ?, ?)", server.getServerId(), server.getIPAddress(), server.getPort(), server.getWipeDate());
        return false;
    }

    @Override
    public Server update(Server server) {
        return null;
    }

    @Override
    public boolean delete(Server server) {
        return false;
    }

    @Override
    public Optional<Server> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Server> getAll() {
        return null;
    }
}
