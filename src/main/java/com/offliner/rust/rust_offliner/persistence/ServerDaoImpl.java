package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.interfaces.IBaseDTOConverter;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.interfaces.IServerValidator;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import com.offliner.rust.rust_offliner.services.BattlemetricsServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerDaoImpl implements IServerValidator {

    @Autowired
    IServerDao serverDao;

    @Autowired
    BattlemetricsServerService service;

    @Autowired
    IBaseDTOConverter<BattlemetricsServerDTO, ServerEntity> converter;

    public ServerEntity save(int id) {
        boolean serverExists = serverDao.existsByServerId(id);
        if (!serverExists) {
            BattlemetricsServerDTO serverData = service.getServer(id);

            serverDao.save(converter.convert(serverData));
        }
        // TODO impl
        return null;
    }

}
