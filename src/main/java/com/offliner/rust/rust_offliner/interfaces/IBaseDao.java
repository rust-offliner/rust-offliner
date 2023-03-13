package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.BaseEntity;
import org.springframework.data.repository.CrudRepository;

public interface IBaseDao extends CrudRepository<BaseEntity, Long> {
}
