package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.MapEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IMapDao extends CrudRepository<MapEntity, Long> {

    // mainly for testing
    @Query(value = "DELETE FROM maps WHERE server_id = :id", nativeQuery = true)
    @Modifying
    void deleteByServerId(long id);

    @Query(value = "UPDATE maps SET current = false WHERE server_id = :id", nativeQuery = true)
    void findByServerIdAndUpdateTracked(long id);

    @Query(value = "SELECT * FROM maps WHERE server_id = :id AND current = true", nativeQuery = true)
    Optional<MapEntity> findByServerAndCurrentIsTrue(long id);
}
