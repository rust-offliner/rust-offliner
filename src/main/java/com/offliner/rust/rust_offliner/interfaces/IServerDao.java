package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IServerDao extends CrudRepository<ServerEntity, Integer> {

    ServerEntity findByServerId(int id);

    boolean existsByServerId(int id);

    long countAllByCurrentlyTrackedIsTrue();

    @Query(nativeQuery = true, value = "SELECT server_id FROM `server` WHERE tracked = 1")
    List<Integer> getAllCurrentlyTrackedServerIds();

    @Modifying
    @Query("UPDATE ServerEntity s SET s.currentlyTracked = :tracked WHERE s.serverId = :server_id")
    void updateTrackedState(@Param(value = "server_id") long id, @Param(value = "tracked") boolean tracked);

}
