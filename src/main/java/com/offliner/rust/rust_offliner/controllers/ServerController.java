package com.offliner.rust.rust_offliner.controllers;

import com.offliner.rust.rust_offliner.services.ServerService;
import com.offliner.rust.rust_offliner.services.service_datamodel.ServerTemplate.ServerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ServerController {

    @Autowired
    ServerService serverService;

    @GetMapping("")
    public String getServer() {
        return serverService.getServer();
    }

}
