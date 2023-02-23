package com.offliner.rust.rust_offliner.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.offliner.rust.rust_offliner.exceptions.CustomMapNotSupportedException;
import com.offliner.rust.rust_offliner.exceptions.MapNotProceduralException;
import com.offliner.rust.rust_offliner.persistence.datamodel.MapEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

public class EServerDto {

    private long id;
    private String name;
    private int currentPlayers;
    private int maxPlayers;

    private String ipAddress;

    private int port;

    private List<PlayerEntity> playerList = new ArrayList<>();

    private LocalDateTime lastWiped;

    public MapEntity getMap() {
        return map;
    }

    public void setMap(MapEntity map) {
        this.map = map;
    }

    private MapEntity map;



    public EServerDto() { }

    public EServerDto(long id) {
        this.id = id;
    }

    public EServerDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     *
     * @param data
     * JSON object, which looks like:
     * {
     *     "data": {
     *         "id": Integer,
     *         ...,
     *         "attributes": {
     *             "name": String,
     *             "ip": Integer,
     *             "port": Integer,
     *             "maxPlayers": Integer,
     *             "details": {
     *                 ...,
     *                 "rust_last_wipe": "String (date)"
     *             }
     *         }
     *     },
     *     ...
     * }
     * @throws ParseException
     */
    @JsonProperty("data")
    private void unpackNestedData(@NotNull Map<String, Object> data) throws ParseException {
        this.setId(Long.parseLong((String) data.get("id")));
        Map<String, Object> attributes = (Map<String, Object>) data.get("attributes");

        this.setName((String) attributes.get("name"));
        this.setCurrentPlayers((Integer) attributes.get("players"));
        this.setMaxPlayers((Integer) attributes.get("maxPlayers"));
        this.setPort((Integer) attributes.get("port"));
        this.setIpAddress((String) attributes.get("ip"));

        Map<String, Object> details = (Map<String, Object>) attributes.get("details");
        String lastWipedString = (String) details.get("rust_last_wipe");


        // todo refactor ??
        try {
            String mapName = (String) details.get("map");
            if (!mapName.equals("Procedural Map")) {
                if (ServersWithCustomMapsSupported.stream()
                        .anyMatch(server ->
                        mapName.toLowerCase()
                        .contains(server.getNameServer().toLowerCase()))
                ) {
                   throw new MapNotProceduralException();
                }
                throw new CustomMapNotSupportedException("Map is custom");
            }
        } catch (CustomMapNotSupportedException e) {
            //TODO
            this.setMap(null);
        } catch (MapNotProceduralException e) {
            /*
            //TODO
            jakas logika której jeszcz enie zrobilem misiaczku
             */
        }

        /**
         * use static function LocalDateTime.parse() to convert String to date
         * remove Z at the end of @param lastWipedString for compatibility
         */
        this.setLastWiped(LocalDateTime.parse(lastWipedString.substring(0, lastWipedString.length() - 1)));
    }

    public LocalDateTime getLastWiped() {
        return lastWiped;
    }

    public void setLastWiped(LocalDateTime lastWiped) {
        this.lastWiped = lastWiped;
    }

    /**
     *
     * @param list
     * list of players with their data currently playing on the server
     */
    @JsonProperty("included")
    private void unpackNestedPlayers(List<Map<String, Object>> list) {
        for (Map<String, Object> player : list) {
            Map<String, String> attributes = (Map<String, String>) player.get("attributes");

            long id = (Long.parseLong((String) player.get("id")));

            String dateString = attributes.get("updatedAt");
            /**
             * use static function LocalDateTime.parse() to convert String to date
             * remove Z at the end of @param lastWipedString for compatibility
             */
            LocalDateTime date = LocalDateTime.parse(dateString.substring(0, dateString.length() - 1));

            String name = attributes.get("name");

            playerList.add(new PlayerEntity(id, name, date));
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

    public List<PlayerEntity> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerEntity> playerList) {
        this.playerList = playerList;
    }

    public boolean isNull() {
        return (id == 0 && name == null && currentPlayers == 0 && maxPlayers == 0 && playerList.isEmpty());
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EServerDto serverDTO = (EServerDto) o;
        return id == serverDTO.id && currentPlayers == serverDTO.currentPlayers && maxPlayers == serverDTO.maxPlayers && name.equals(serverDTO.name) && playerList.equals(serverDTO.playerList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currentPlayers, maxPlayers, playerList);
    }

    @Override
    public String toString() {
        return "EServerDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPlayers=" + currentPlayers +
                ", maxPlayers=" + maxPlayers +
//                ", playerList=" + playerList +
                ", lastWiped=" + lastWiped +
                ", map=" + map +
                '}';
    }
}
