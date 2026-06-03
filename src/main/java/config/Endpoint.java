package config;

import lombok.Getter;

@Getter
public enum Endpoint {

    BOARDS("/boards"),
    CARDS("/cards"),
    LISTS("/lists");


    private final String url;

    Endpoint(String url) {
        this.url = url;
    }
}
