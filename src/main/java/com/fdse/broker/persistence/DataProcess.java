package com.fdse.broker.persistence;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DataProcess
 * @Description TODO
 * @Author LiuJJ
 * @Date 2019/1/11 16:22
 * @Version 1.0
 **/
public class DataProcess {
    public static Map<String,String> map = new HashMap<>();
    String serviceName;
    String serviceURL;
    String requestMethod;
    String serviceInput;
    String serviceOutput;

    public void getMap(String serviceDescription){
        // 调用信息服务 将返回的参数存入流程变量中
        JSONObject jb = JSONObject.fromObject(serviceDescription);
        serviceName = jb.get("serviceName").toString();
        serviceURL = jb.get("serviceURL").toString();
        requestMethod = jb.get("requestMethod").toString();

        serviceInput = JSONObject.fromObject(jb.get("serviceInput")).toString();
        serviceOutput = JSONObject.fromObject(jb.get("serviceOutput")).toString();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getServiceInput() {
        return serviceInput;
    }

    public void setServiceInput(String serviceInput) {
        this.serviceInput = serviceInput;
    }

    public String getServiceOutput() {
        return serviceOutput;
    }

    public void setServiceOutput(String serviceOutput) {
        this.serviceOutput = serviceOutput;
    }
}
