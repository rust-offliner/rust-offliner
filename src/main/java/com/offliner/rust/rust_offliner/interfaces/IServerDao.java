package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface IServerDao extends CrudRepository<ServerEntity, Integer> {

    Optional<ServerEntity> findByServerId(long id);

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
    @Query(value = "DELETE FROM `servers_to_users`", nativeQuery = true)
    void clearServersToUsersTable();

    @Modifying
    @Query(value = "UPDATE ServerEntity s SET s.tracked = :b WHERE s.serverId = :id")
    void updateTrackedState(long id, boolean b);

    @Query(value = "SELECT COUNT(*) FROM server INNER JOIN servers_to_users stu on server.server_id = stu.server_id WHERE stu.server_id = :id1 AND user_id = :id2", nativeQuery = true)
    long countAllByUsersIsAndServerIdEquals(long id1, long id2);

    @Query(value = "SELECT COUNT(*) FROM servers_to_users WHERE server_id = :server_id AND user_id = :user_id", nativeQuery = true)
    long findByUserIdAndServerId(long server_id, long user_id);

    @EntityGraph("Server.users")
    ServerEntity getServerEntityGraphByServerId(long id);

//    @Query(value = "SELECT ", nativeQuery = true)
//    boolean existsByServerIdAndUserId();
}
