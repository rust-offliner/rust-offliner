package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.data.repository.CrudRepository;

public interface IServerDao extends CrudRepository<ServerEntity, Integer> {

    ServerEntity findByServerId(int id);

    boolean existsByServerId(int id);

}
