package com.digital.harrier.error.handling.beans;

import java.io.Serializable;

public class CloudQueueConfigure implements Serializable{

    private String name;
    private String secretId;
    private String secret;
    private String endPoint;
    private int waitSeconds = 10;
    private String description;

    public CloudQueueConfigure(String name, String secretId, String secret, String endPoint, int waitSeconds, String description) {
        this.name = name;
        this.secretId = secretId;
        this.secret = secret;
        this.endPoint = endPoint;
        this.waitSeconds = waitSeconds;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public int getWaitSeconds() {
        return waitSeconds;
    }

    public void setWaitSeconds(int waitSeconds) {
        this.waitSeconds = waitSeconds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
