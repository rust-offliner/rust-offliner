package com.offliner.rust.rust_offliner.datamodel.converters;

import com.offliner.rust.rust_offliner.datamodel.PlayerDTO;
import com.offliner.rust.rust_offliner.interfaces.IBaseDTOConverter;
import com.offliner.rust.rust_offliner.persistence.datamodel.PlayerEntity;

public class PlayerDTOConverter implements IBaseDTOConverter<PlayerEntity, PlayerDTO> {

    @Override
    public PlayerDTO convert(PlayerEntity from) {

        PlayerDTO player = new PlayerDTO();

        player.setName(from.getName());
        player.setBase(from.getBase());
        player.setLastSeen(from.getLastSeen());
        player.setFollowed(from.isFollowed());

        return player;
    }

}
