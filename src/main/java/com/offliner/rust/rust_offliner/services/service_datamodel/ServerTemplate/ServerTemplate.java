package com.offliner.rust.rust_offliner.services.service_datamodel.ServerTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.offliner.rust.rust_offliner.persistence.datamodel.Player;

import java.time.LocalDateTime;
import java.util.*;

public class ServerTemplate {

    private long id;
    private String name;
    private int currentPlayers;
    private int maxPlayers;

    private List<Player> playerList = new ArrayList<>();

    public ServerTemplate() { }

    public ServerTemplate(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty("data")
    private void unpackNestedData(Map<String, Object> data) {
        this.setId(Long.parseLong((String) data.get("id")));
        Map<String, Object> attributes = (Map<String, Object>) data.get("attributes");

        this.setName((String) attributes.get("name"));
        this.setCurrentPlayers((Integer) attributes.get("players"));
        this.setMaxPlayers((Integer) attributes.get("maxPlayers"));
    }

    @JsonProperty("included")
    private void unpackNestedPlayers(List<Map<String, Object>> list) {
        for (Map<String, Object> player : list) {
            Map<String, String> attributes = (Map<String, String>) player.get("attributes");

            long id = (Long.parseLong((String) player.get("id")));

            String dateString = attributes.get("updatedAt");
            LocalDateTime date = LocalDateTime.parse(dateString.substring(0, dateString.length() - 1)); //remove Z at the end for compatibility

            String name = attributes.get("name");

            playerList.add(new Player(id, name, date));
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
