package com.offliner.rust.rust_offliner.maps;

import com.offliner.rust.rust_offliner.exceptions.ImageExtensionNotSupportedException;
import com.offliner.rust.rust_offliner.exceptions.MapSizeInvalidException;
import com.offliner.rust.rust_offliner.exceptions.PrecedentEntityNotExistsException;
import com.offliner.rust.rust_offliner.exceptions.UnprocessableMapImageException;
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
    public void save(long id, MapImage image, int size) throws UnprocessableMapImageException, ImageExtensionNotSupportedException, PrecedentEntityNotExistsException, MapSizeInvalidException {
        if (!checkMapSize(size))
            throw new MapSizeInvalidException();
        Optional<ServerEntity> serverEntity = serverDao.findByServerId(id);
        if (serverEntity.isEmpty())
            throw new PrecedentEntityNotExistsException(PrecedentEntityNotExistsException.Types.SERVER);
        String path = writer.saveImage(id, image);
        MapEntity map = new MapEntity();
        map.setServer(serverEntity.get());
        map.setSize(size);
        map.setImagePath(path);
        mapDao.save(map);
    }

    // this function is for maps directly uploaded by the user
    public void save(long id, MapImage image) throws UnprocessableMapImageException, ImageExtensionNotSupportedException, PrecedentEntityNotExistsException {
        Optional<ServerEntity> serverEntity = serverDao.findByServerId(id);
        if (serverEntity.isEmpty())
            throw new PrecedentEntityNotExistsException(PrecedentEntityNotExistsException.Types.SERVER);
        String path = writer.saveImage(id, image);
        MapEntity map = new MapEntity();
        map.setServer(serverEntity.get());
        map.setImagePath(path);
        map.setTime(Instant.now());
        mapDao.save(map);
    }

    // map is size [1000;6000] with step 50
    private boolean checkMapSize(int size) {
        if (size <= 1000 || size >= 6000)
            return false;
        return size % 50 == 0;
    }

}
