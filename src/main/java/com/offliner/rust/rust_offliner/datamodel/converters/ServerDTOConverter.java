package com.offliner.rust.rust_offliner.datamodel.converters;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.interfaces.IBaseDTOConverter;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ServerDTOConverter implements IBaseDTOConverter<BattlemetricsServerDTO, ServerEntity> {
    @Override
    public ServerEntity convert(BattlemetricsServerDTO from) {
        ServerEntity serverEntity = new ServerEntity();

        serverEntity.setServerId(from.getId());
        serverEntity.setFollowedPlayersList(new ArrayList<>());
        serverEntity.setWipeDate(from.getLastWiped());
        serverEntity.setMap(from.getMap());
        serverEntity.setPlayersCount(from.getCurrentPlayers());
        serverEntity.setPort(from.getPort());
        serverEntity.setAddressIp(from.getIpAddress());

        return serverEntity;
    }
}
