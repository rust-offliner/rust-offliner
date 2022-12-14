package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.Server;
import org.springframework.data.repository.CrudRepository;

public interface IServerDao extends CrudRepository<Server, Integer> {

    Server findByServerId(int id);

}
