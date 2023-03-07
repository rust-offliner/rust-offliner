package com.offliner.rust.rust_offliner.maps;

import com.offliner.rust.rust_offliner.exceptions.maps.ImageExtensionNotSupportedException;

public enum ImageExtension {
    PNG, JPEG, UNKNOWN;

    public String value() throws ImageExtensionNotSupportedException {
        return switch (this) {
            case PNG -> ".png";
            case JPEG -> ".jpg";
            case UNKNOWN -> throw new ImageExtensionNotSupportedException();
        };
    }
}
