package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.services.BattlemetricsServerService;
import com.offliner.rust.rust_offliner.services.ServerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ServerController {

    @Autowired
    BattlemetricsServerService serverService;

    @GetMapping("/{id}")
    public ServerTemplate getServer(@PathVariable int id) {
        return serverService.getServer(id);
    }

//    @PostMapping("query")
//    public Server returnServer(@RequestBody String query) {  }

}
