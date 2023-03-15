package com.offliner.rust.rust_offliner.bases;

import com.offliner.rust.rust_offliner.datamodel.BaseDto;
import com.offliner.rust.rust_offliner.datamodel.converters.MapDtoConverter;
import com.offliner.rust.rust_offliner.exceptions.bases.CoordsOutOfBoundsException;
import com.offliner.rust.rust_offliner.interfaces.IBaseDao;
import com.offliner.rust.rust_offliner.persistence.datamodel.BaseEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.MapEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseManager {

    final MapDtoConverter converter;

    final IBaseDao dao;

    public BaseManager(MapDtoConverter converter, IBaseDao dao) {
        this.converter = converter;
        this.dao = dao;
    }

    public void save(BaseDto dto) throws CoordsOutOfBoundsException {
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
        dao.save(base);
    }
}
