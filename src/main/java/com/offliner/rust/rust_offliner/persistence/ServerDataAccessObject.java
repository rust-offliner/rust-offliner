package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.persistence.datamodel.Server;
import com.offliner.rust.rust_offliner.interfaces.DataAccessObject;

import java.util.List;
import java.util.Optional;

public class ServerDataAccessObject implements DataAccessObject<Server> {


    @Override
    public boolean add() {
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
