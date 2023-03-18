package com.offliner.rust.rust_offliner.datamodel;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BaseDto {
    @Min(value = 1, message = "Server id must be greater than 0")
    private long serverId;
    @Min(value = 0, message = "Coordinates must be greater than 0")
    private int x;
    @Min(value = 0, message = "Coordinates must be greater than 0")
    private int y;
    private boolean friendly;
}
