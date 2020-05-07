package com.digital.harrier.error.handling.beans;

public class ListenerConfigure {
    private String name;
    private String queueName;
    private String type;
    private int executorPoolSize;


    public ListenerConfigure(String name, String queueName, String type, int executorPoolSize) {
        this.name = name;
        this.queueName = queueName;
        this.type = type;
        this.executorPoolSize = executorPoolSize;

    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getQueueName() {
        return queueName;
    }


    public int getExecutorPoolSize() {
        return executorPoolSize;
    }

}
