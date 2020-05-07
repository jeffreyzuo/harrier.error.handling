package com.digital.harrier.error.handling.helper;


import com.qcloud.cmq.client.consumer.Consumer;
import com.qcloud.cmq.client.producer.Producer;

public class TxCmqHelper {

    private String nameServerAddr;
    private String secretId;
    private String secretKey;
    private int retryTimes;
    private int timeout;

    public TxCmqHelper(String nameServerAddr, String secretId, String secretKey) {
        this(nameServerAddr,secretId,secretKey,3,5000);
    }

    public TxCmqHelper(String nameServerAddr, String secretId, String secretKey, int retryTimes, int timeout) {
        this.nameServerAddr = nameServerAddr;
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.retryTimes = retryTimes;
        this.timeout = timeout;
    }

    public  Producer newProducer() {
        Producer producer = new Producer();
        producer.setNameServerAddress(nameServerAddr);
        producer.setSecretId(secretId);
        producer.setSecretKey(secretKey);
        producer.setRetryTimesWhenSendFailed(retryTimes);
        producer.setRequestTimeoutMS(timeout);
        producer.start();
        return producer;
    }

    public Consumer newConsumer(int batchPullNumber) {
        Consumer consumer = new Consumer();
        consumer.setNameServerAddress(nameServerAddr);
        consumer.setSecretId(secretId);
        consumer.setSecretKey(secretKey);
        consumer.setBatchPullNumber(batchPullNumber);
        consumer.setRequestTimeoutMS(timeout);
        consumer.start();
        return consumer;
    }

    public Consumer newConsumer() {
        return newConsumer(10);
    }

}
