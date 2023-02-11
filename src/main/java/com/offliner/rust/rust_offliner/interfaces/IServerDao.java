package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IServerDao extends CrudRepository<ServerEntity, Integer> {

    ServerEntity findByServerId(int id);

    boolean existsByServerId(int id);

    long countAllByCurrentlyTrackedIsTrue();

    @Query(nativeQuery = true, value = "SELECT server_id FROM `server` WHERE tracked = 1")
    List<Long> getAllByServerIdAndCurrentlyTrackedIsTrue();
}
