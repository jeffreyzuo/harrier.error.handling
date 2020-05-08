package com.digital.harrier.error.handling.listener;

import com.digital.harrier.common.helper.JedisHelper;
import com.digital.harrier.common.helper.OkHttpClientHelper;
import com.digital.harrier.error.handling.beans.ErrorRise;
import com.digital.harrier.error.handling.constracts.ReceiptDeleteCallBack;

import com.digital.harrier.error.handling.helper.TxCmqHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qcloud.cmq.client.consumer.BatchReceiveResult;
import com.qcloud.cmq.client.consumer.Consumer;
import com.qcloud.cmq.client.consumer.DeleteResult;
import com.qcloud.cmq.client.consumer.Message;
import com.qcloud.cmq.client.exception.MQClientException;
import com.qcloud.cmq.client.exception.MQServerException;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class TxCmqListener implements Runnable,ReceiptDeleteCallBack {

    private TxCmqHelper txCmqHelper;
    private String queueName;
    private String messageUrl;

    private Logger logger = Logger.getLogger(TxCmqHelper.class);
    private Consumer consumer;
    private int batchPullNumber = 10;
    private boolean stop = false;
    private ExecutorService executorService;

    public TxCmqListener(TxCmqHelper txCmqHelper, String queueName,int executorServicePoolSize) {
        this.txCmqHelper = txCmqHelper;
        this.queueName = queueName;

        this.executorService = Executors.newFixedThreadPool(executorServicePoolSize);
    }


    @Override
    public void run() {
        consumer = getTxCmqHelper().newConsumer(batchPullNumber);

        getLogger().debug("TxCmqListener running for queue:" + getQueueName() + ",start with threads:" + consumer.getClientCallbackExecutorThreads());

        BatchReceiveResult batchResult;

        while (!stop) {
            batchResult = null;
            try {
                batchResult = getConsumer().batchReceiveMsg(getQueueName(), getBatchPullNumber());

            } catch (MQClientException e ) {
                getLogger().error(e.getMessage());
                sleep(5000);
                continue;
            } catch (MQServerException e) {
                getLogger().error(e.getMessage());

                sleep(5000);
                continue;
            }
            if (batchResult == null) {
                sleep(100);
                continue;
            }

            if(batchResult.getReturnCode()!=0) {
                if(batchResult.getReturnCode()==10200) { //empty message
                    sleep(200);
                    continue;
                }
                //Batch receive error,code 10100,error msg:(10100)queue is not existed, or deleted
                //getLogger().error("Batch receive error,code " + batchResult.getReturnCode() + ",error msg:" + batchResult.getErrorMessage());
                sleep(200);
                continue;
            }

            List<Message> msgList = batchResult.getMessageList();
            //List<Long> receiptHandlerArray = new ArrayList<>();
            Gson gson = new Gson();
            ErrorRise errorRise;
            for (Message msg : msgList) {
                //getLogger().debug("Got message:" + msg.getData());
                try {
                    errorRise = gson.fromJson(msg.getData(),ErrorRise.class);
                } catch (JsonSyntaxException e) {
                    getLogger().error(e.getMessage());
                    getConsumer().deleteMsg(getQueueName(),msg.getReceiptHandle());
                    continue;
                }

                if (errorRise == null) {
                    getLogger().error("Bad message." + msg.getData());
                    continue;
                }

                deleteMsg(msg.getReceiptHandle());

                getLogger().debug("Form:" + errorRise.getFrom() + ",component:" + errorRise.getComponentName() + ",appid:" + errorRise.getAppid() + ",error code:" + errorRise.getErrCode() + ",error msg:" + errorRise.getErrMsg() + ",message:" + errorRise.getMessage());

            }


        }
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    @Override
    public void deleteMsg(long receiptHandler) {
        DeleteResult deleteResult = getConsumer().deleteMsg(getQueueName(),receiptHandler);
        if(deleteResult.getReturnCode()!=0) {
            getLogger().error("delete msg error:" + deleteResult.getErrorMessage() + ",receipt handler:" + receiptHandler);
        }
    }


    public TxCmqHelper getTxCmqHelper() {
        return txCmqHelper;
    }

    private void reBuildConsumer() {
        sleep(3000);
        getLogger().info("Try to shutdown old consumer.");
        getConsumer().shutdown();
        getLogger().info("To create new consumer");
        setConsumer(txCmqHelper.newConsumer());
        getLogger().info("Renew consumer done!");
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public String getQueueName() {
        return queueName;
    }

    public int getBatchPullNumber() {
        return batchPullNumber;
    }

    public Logger getLogger() {
        return logger;
    }
    private void sleep(int sleepMills) {
        try {
            Thread.sleep(sleepMills);
        } catch (InterruptedException e) {
            getLogger().error(e.getMessage());
        }
    }



    public ExecutorService getExecutorService() {
        return executorService;
    }



}
