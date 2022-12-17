package com.offliner.rust.rust_offliner.datamodel;

import java.util.stream.Stream;

/**
 * servers that have publicly available API to get their custom map PNG
 */
public enum ServersWithCustomMapsSupported {

    RUSTICATED("Rusticated"),
    RUSTORIA("Rustoria");

    private String nameServer;

    ServersWithCustomMapsSupported(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public static Stream<ServersWithCustomMapsSupported> stream() {
        return Stream.of(ServersWithCustomMapsSupported.values());
    }
}
