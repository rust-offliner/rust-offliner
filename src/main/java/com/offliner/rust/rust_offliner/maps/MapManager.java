package com.offliner.rust.rust_offliner.maps;

import com.offliner.rust.rust_offliner.exceptions.maps.ImageExtensionNotSupportedException;
import com.offliner.rust.rust_offliner.exceptions.maps.MapSizeInvalidException;
import com.offliner.rust.rust_offliner.exceptions.PrecedentEntityNotExistsException;
import com.offliner.rust.rust_offliner.exceptions.maps.UnprocessableMapImageException;
import com.offliner.rust.rust_offliner.interfaces.IMapDao;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.persistence.datamodel.MapEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class MapManager {

    MapFileWriter writer;

    IMapDao mapDao;

    IServerDao serverDao;

    public MapManager(MapFileWriter writer, IMapDao mapDao, IServerDao serverDao) {
        this.writer = writer;
        this.mapDao = mapDao;
        this.serverDao = serverDao;
    }

    // this function is for "scrapped" maps from RustMaps or directly from server owner
    // this should not be used in controller
    public void serverSave(long id, MapImage image, int size) throws UnprocessableMapImageException, ImageExtensionNotSupportedException, PrecedentEntityNotExistsException, MapSizeInvalidException {
        if (!checkMapSize(size))
            // shouldn't be thrown ever because the data is generated by origin
            throw new MapSizeInvalidException();
        MapEntity map = new MapEntity();
        save(id, image, map);
        // time is wipe time
        map.setSize(size);
        mapDao.save(map);
    }

    // this function is for maps directly uploaded by the user
    public void userSave(long id, MapImage image) throws UnprocessableMapImageException, ImageExtensionNotSupportedException, PrecedentEntityNotExistsException {
        MapEntity map = new MapEntity();
        save(id, image, map);
        map.setTime(Instant.now());
        // size is irrelevant because we don't need to put "square mesh" onto map
        mapDao.save(map);
    }

    private String save(long id, MapImage image, MapEntity map) throws PrecedentEntityNotExistsException, UnprocessableMapImageException, ImageExtensionNotSupportedException {
        Optional<ServerEntity> serverEntity = serverDao.findByServerId(id);
        if (serverEntity.isEmpty())
            throw new PrecedentEntityNotExistsException(PrecedentEntityNotExistsException.Types.SERVER);

        // we must clear database to only have 1 current map for each server
        mapDao.findByServerIdAndUpdateTracked(id);

        String path = writer.saveImage(id, image);
        map.setCurrent(true);
        map.setServer(serverEntity.get());
        map.setImagePath(path);
        return path;
    }

    // map is size [1000;6000] with step 50
    private boolean checkMapSize(int size) {
        if (size <= 1000 || size >= 6000)
            return false;
        return size % 50 == 0;
    }

}
