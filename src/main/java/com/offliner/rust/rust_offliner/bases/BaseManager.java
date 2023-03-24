package com.offliner.rust.rust_offliner.bases;

import com.offliner.rust.rust_offliner.datamodel.BaseDto;
import com.offliner.rust.rust_offliner.datamodel.converters.MapDtoConverter;
import com.offliner.rust.rust_offliner.exceptions.PrecedentEntityNotExistsException;
import com.offliner.rust.rust_offliner.exceptions.bases.CoordsOutOfBoundsException;
import com.offliner.rust.rust_offliner.interfaces.IBaseDao;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.persistence.datamodel.BaseEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.MapEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BaseManager {

    final MapDtoConverter converter;

    final IBaseDao mapDao;

    final IServerDao serverDao;

    public BaseManager(MapDtoConverter converter, IBaseDao dao, IServerDao serverDao) {
        this.converter = converter;
        this.mapDao = dao;
        this.serverDao = serverDao;
    }

    public void save(@NotNull BaseDto dto) throws CoordsOutOfBoundsException, PrecedentEntityNotExistsException {
        Optional<ServerEntity> server = serverDao.findByServerId(dto.getServerId());
        // we cant add a base to a server that doesn't exist
        if (server.isEmpty()) {
            throw new PrecedentEntityNotExistsException(PrecedentEntityNotExistsException.Types.MAP);
        }
        BaseEntity base = converter.convert(dto);
        MapEntity map = base.getMap();
        if (map != null) {
            int size = map.getSize();
            // map is not obtained from user screenshot
            if (size != 0) {
                if (base.getCoordX() > size || base.getCoordY() > size) {
                    throw new CoordsOutOfBoundsException();
                }
            }
        }
        mapDao.save(base);
    }
}
