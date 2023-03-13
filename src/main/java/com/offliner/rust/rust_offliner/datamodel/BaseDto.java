package com.offliner.rust.rust_offliner.datamodel;

import lombok.Data;

@Data
public class BaseDto {
    private long id;
    private int x;
    private int y;
    private boolean friendly;
}
