package com.offliner.rust.rust_offliner.datamodel.converters;

import com.offliner.rust.rust_offliner.datamodel.PlayerDTO;
import com.offliner.rust.rust_offliner.interfaces.IBaseDTOConverter;
import com.offliner.rust.rust_offliner.persistence.datamodel.Player;

public class PlayerDTOConverter implements IBaseDTOConverter<Player, PlayerDTO> {

    @Override
    public PlayerDTO convert(Player from) {

        PlayerDTO player = new PlayerDTO();

        player.setName(from.getName());
        player.setBase(from.getBase());
        player.setLastSeen(from.getLastSeen());
        player.setFollowed(from.isFollowed());

        return player;
    }

}
