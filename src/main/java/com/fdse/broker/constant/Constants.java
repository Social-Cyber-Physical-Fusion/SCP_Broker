package com.fdse.broker.constant;

/**
 * @ClassName Constants
 * @Description TODO
 * @Author LiuJJ
 * @Date 2019/1/9 14:50
 * @Version 1.0
 **/
public class Constants {
    /**
     * App后端url
     */
    public static final String APP_BACK_END_IP = "192.168.1.115";//142
    public static final String APP_BACK_END_PORT = "8080";

    //获取用户位置
    public static final String APP_BACK_END_USER_GET_LOCATION = "user/getLocation";

    //发送消息
    public static final String APP_BACK_END_SEND_MESSAGE_TO_MP = "user/getLocation";

    public static String getAppBackEndServiceURL(String  service) {
        String serviceURL = String.format("http://%s:%s/%s", APP_BACK_END_IP, APP_BACK_END_PORT, service);
        return serviceURL;
    }
    /**
     * 知识库
     */
    public static final String KG = "http://10.141.221.88:36464/KG_Search";

    //流程仓库
    public static final String APP = "http://192.168.1.168:8080/user/sendMessageToMPOne";

    public static final String CS = "http://192.168.1.168:8081/simplecrowdsourcing_war_exploded/complete/completetask";
    /**
     * HASS
     */
    public static final String HASS_IP = "10.141.221.88";//TODO
    public static final String HASS_PORT = "8123";
    public static final String HASS_Entity = "/api";
    public static String getHassState(String entity_id){
        String url = String.format("http://%s:%s%s%s", HASS_IP, HASS_PORT,HASS_Entity, entity_id);
        return url;
    }
    /**
     * 第三方信息服务url
     */
    public static final String Information_Service_IP = "192.168.1.136";//TODO
    public static final String Information_Service_PORT = "8080";
    public static String getInformation_Service_URL(String  service) {
        String serviceURL = String.format("http://%s:%s/%s", Information_Service_IP, Information_Service_PORT, service);
        return serviceURL;
    }

    //获取热水

    //获取指令

    public static final String REST_API_ServiceURLQuery = "http://47.100.23.182:8004/query/serviceParamQuery";
    public static final String FIND_SERVICE_BY_NAME = "http://10.141.221.88:5001/find_service_by_name";
    /**
     * 本体库平台url
     */
    public static final String ONTOLOGY_IP = "192.168.1.142";//142
    public static final String ONTOLOGY_PORT = "8080";

    //用户登录
    public static final String ONTOLOGY_GET_OWLS = "getOwls";

    public static String getOntologyServiceURL(String  service) {
        String serviceURL = String.format("http://%s:%s/%s", ONTOLOGY_IP, ONTOLOGY_PORT, service);
        return serviceURL;
    }

    /**
     * 流程执行引擎url
     */
    public static final String ACTIVITI_IP = "http://192.168.1.116";
    public static final String ACTIVITI_PORT = "8003";
    public static String getActiviti(){
        return ACTIVITI_IP + ":" + ACTIVITI_PORT;
    }

    /**
     * 众包流程执行引擎url
     */
    public static final String CSACTIVITI_IP = "http://192.168.1.194";
    public static final String CSACTIVITI_PORT = "8084";
    public static String getCSActiviti(){
        return CSACTIVITI_IP + ":" + CSACTIVITI_PORT;
    }


    //用户登录
    public static final String ACTIVITI_GET_BPMN= "getOwls";

    public static String getActivitiServiceURL(String  service) {
        String serviceURL = String.format("http://%s:%s/%s", ACTIVITI_IP, ACTIVITI_PORT, service);
        return serviceURL;
    }

    /**
     * 众包平台url
     */
}
