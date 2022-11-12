package com.offliner.rust.rust_offliner.services.service_datamodel.ServerTemplate;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "players",
        "maxPlayers"
})
@Generated("jsonschema2pojo")
public class Attributes {

    @JsonProperty("name")
    private String name;
    @JsonProperty("players")
    private Integer players;
    @JsonProperty("maxPlayers")
    private Integer maxPlayers;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("players")
    public Integer getPlayers() {
        return players;
    }

    @JsonProperty("players")
    public void setPlayers(Integer players) {
        this.players = players;
    }

    @JsonProperty("maxPlayers")
    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    @JsonProperty("maxPlayers")
    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

