package com.offliner.rust.rust_offliner.interfaces;

import com.offliner.rust.rust_offliner.persistence.datamodel.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends CrudRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);

}
