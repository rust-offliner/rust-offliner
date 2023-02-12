package com.offliner.rust.rust_offliner.persistence;

import com.offliner.rust.rust_offliner.datamodel.BattlemetricsServerDTO;
import com.offliner.rust.rust_offliner.exceptions.KeyAlreadyExistsException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServerDataStateManager {

    private Map<Long, BattlemetricsServerDTO> state = Collections.synchronizedMap(new HashMap<>());

    public boolean add(long id, BattlemetricsServerDTO server) throws KeyAlreadyExistsException {
        if (state.containsKey(id)) {
            throw new KeyAlreadyExistsException("The key you are trying to put already exists");
        }
        state.put(id, server);
        return true;
    }
}
