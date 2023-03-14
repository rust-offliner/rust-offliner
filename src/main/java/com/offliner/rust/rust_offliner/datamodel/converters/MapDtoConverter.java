package com.offliner.rust.rust_offliner.datamodel.converters;

import com.offliner.rust.rust_offliner.datamodel.BaseDto;
import com.offliner.rust.rust_offliner.interfaces.IBaseDTOConverter;
import com.offliner.rust.rust_offliner.interfaces.IMapDao;
import com.offliner.rust.rust_offliner.persistence.datamodel.BaseEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.MapEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class MapDtoConverter implements IBaseDTOConverter<BaseDto, BaseEntity> {

    final IMapDao dao;

    public MapDtoConverter(IMapDao dao) {
        this.dao = dao;
    }

    @Override
    public BaseEntity convert(BaseDto from) {
        BaseEntity base = new BaseEntity(
                from.getX(),
                from.getY()
        );
        Optional<MapEntity> map = dao.findByServerAndCurrentIsTrue(from.getId());
        map.ifPresent(base::setMap);
        base.setPlayerList(new ArrayList<>());
        return base;
    }
}
