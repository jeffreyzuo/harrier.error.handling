package com.digital.harrier.error.handling.beans;

import java.io.Serializable;

public class RedisServer implements Serializable{
    String name;
    String host;
    int port;
    String auth;
    String description;

    public RedisServer(String name, String host, int port, String auth, String description) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.description = description;
    }
    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getAuth() {
        return auth;
    }

    public String getDescription() {
        return description;
    }
}
