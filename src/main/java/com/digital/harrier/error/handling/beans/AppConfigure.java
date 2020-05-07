package com.digital.harrier.error.handling.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppConfigure {
    private String version;
    private Map<String,RedisServer> redisServerMap = new HashMap<>();
    private Map<String,CloudQueueConfigure> cloudQueueConfigureMap = new HashMap<>();
    private List<ListenerConfigure> listenerConfigures = new ArrayList<>();

    public void addCloudQueueConfigure(String name,CloudQueueConfigure cloudQueueConfigure) {
        cloudQueueConfigureMap.put(name,cloudQueueConfigure);
    }

    public CloudQueueConfigure getCloudQueueConfigure(String name) {
        return cloudQueueConfigureMap.getOrDefault(name,null);
    }

    public void addRedisServer(String name,RedisServer redisServer) {
        this.redisServerMap.put(name,redisServer);
    }

    public RedisServer getRedisServer(String name) {
        return this.redisServerMap.getOrDefault(name,null);
    }

    public void addListener(ListenerConfigure listenerConfigure) {
        listenerConfigures.add(listenerConfigure);
    }

    public List<ListenerConfigure> getListenerConfigures() {
        return listenerConfigures;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getVersion() {
        return version;
    }
}
