package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IServerDao extends CrudRepository<ServerEntity, Integer> {

    ServerEntity findByServerId(long id);

    boolean existsByServerId(long id);

//    long countAllByCurrentlyTrackedIsTrue();
//    long countAllBy

    @Query(nativeQuery = true, value = "SELECT server_id FROM `server` WHERE tracked = 1")
    List<Integer> getAllByTrackedIsTrue();

    long countAllByTrackedIsTrue();

    @Modifying
    @Query(value = "UPDATE ServerEntity s SET s.tracked = 0")
    void initTrackedState();

    @Modifying
    @Query(value = "UPDATE ServerEntity s SET s.tracked = :b WHERE s.serverId = :id")
    void updateTrackedState(long id, boolean b);
}
