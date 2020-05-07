package com.digital.harrier.error.handling;

import com.digital.harrier.common.helper.XmlConfigureHelper;
import com.digital.harrier.error.handling.beans.AppConfigure;
import com.digital.harrier.error.handling.beans.CloudQueueConfigure;
import com.digital.harrier.error.handling.beans.ListenerConfigure;
import com.digital.harrier.error.handling.helper.AppConfigureHelper;
import com.digital.harrier.error.handling.helper.TxCmqHelper;
import com.digital.harrier.error.handling.listener.TxCmqListener;
import org.apache.log4j.Logger;
import org.dom4j.Document;

public class App {
    private final static Logger logger = Logger.getLogger(App.class);
    public static void main(String[] args) {
        Document document = XmlConfigureHelper.getDocument("config.xml");
        if(document == null) {
            logger.error("Load config file failed!");
            System.exit(-1);
        }
        AppConfigure appConfigure = AppConfigureHelper.loadConfigure(document);

        if (appConfigure==null) {
            logger.error("Load config file failed!!");
            System.exit(-1);
        }
        CloudQueueConfigure txCmqConfigure = appConfigure.getCloudQueueConfigure("tx_cmq");

        TxCmqHelper txCmqHelper = new TxCmqHelper(txCmqConfigure.getEndPoint(),txCmqConfigure.getSecretId(),txCmqConfigure.getSecret());

        for (ListenerConfigure listenerConfigure:appConfigure.getListenerConfigures()
                ) {
            TxCmqListener cmqListener = new TxCmqListener(txCmqHelper,listenerConfigure.getQueueName(),listenerConfigure.getExecutorPoolSize());
            Thread t = new Thread(cmqListener,listenerConfigure.getName());
            t.start();
            logger.debug("Thread started:" + t.getName());
        }

    }
}
