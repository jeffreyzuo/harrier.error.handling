package com.digital.harrier.error.handling.helper;

import com.digital.harrier.common.helper.XmlConfigureHelper;
import com.digital.harrier.error.handling.beans.*;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public class AppConfigureHelper {

    public static AppConfigure loadConfigure(Document document) {
        AppConfigure appConfigure = new AppConfigure();


        String version = XmlConfigureHelper.getItemValue(document.getRootElement(),"version");
        appConfigure.setVersion(version);


        Element redis_servers = document.getRootElement().element("redis-servers");
        if(redis_servers != null) {
            List<Element> elements = redis_servers.elements();
            for (Element element:elements
                    ) {
                String name = XmlConfigureHelper.getItemValue(element,"name");
                String host = XmlConfigureHelper.getItemValue(element,"host");
                int port = XmlConfigureHelper.getIntItemValue(element,"port");
                String auth = XmlConfigureHelper.getItemValue(element,"auth");
                String description = XmlConfigureHelper.getItemValue(element,"description");
                appConfigure.addRedisServer(name,new RedisServer(name,host,port,auth,description));
            }
        }

        Element cloud_queues = document.getRootElement().element("cloud_queues");
        if(cloud_queues != null) {
            List<Element> elements = cloud_queues.elements();
            for (Element e:elements
                 ) {
                String name = XmlConfigureHelper.getItemValue(e,"name");
                String secret_id = XmlConfigureHelper.getItemValue(e,"secret_id");
                String secret = XmlConfigureHelper.getItemValue(e,"secret");
                String end_point = XmlConfigureHelper.getItemValue(e,"end_point");
                String description = XmlConfigureHelper.getItemValue(e,"description");
                int waitSeconds = 0;
                waitSeconds = XmlConfigureHelper.getIntItemValue(e,"receive_wait_seconds");
                appConfigure.addCloudQueueConfigure(name,new CloudQueueConfigure(name,secret_id,secret,end_point,waitSeconds,description));
            }
        }


        Element listeners = document.getRootElement().element("listeners");
        if (listeners != null) {
            List<Element> elements = listeners.elements();
            for (Element e:elements) {
                String name = XmlConfigureHelper.getItemValue(e,"name");
                String queue_name = XmlConfigureHelper.getItemValue(e,"queue-name");
                String type = XmlConfigureHelper.getItemValue(e,"type");
                int pool_size = XmlConfigureHelper.getIntItemValue(e,"executor-pool-size");

                appConfigure.addListener(new ListenerConfigure(name,queue_name,type,pool_size));
            }
        }


        return appConfigure;
    }

}
