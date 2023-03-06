package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.MapEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IMapDao extends CrudRepository<MapEntity, Long> {

    // mainly for testing
    @Query(value = "DELETE FROM maps WHERE server_id = :id", nativeQuery = true)
    @Modifying
    void deleteByServerId(long id);
}
