package com.fdse.broker.controller;

import com.fdse.broker.constant.Constants;
import com.fdse.broker.http.HttpRequestor;
import com.fdse.broker.persistence.DataProcess;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MasterController
 * @Description TODO
 * @Author LiuJJ
 * @Date 2019/1/9 14:00
 * @Version 1.0
 **/
@RestController(value = "/broker")
public class MasterController {
    Logger logger = LoggerFactory.getLogger(MasterController.class);

    public RestTemplate restTemplate = new RestTemplate();
    private static Map<String,String> taskMap = new HashMap<>();
    @RequestMapping(value = "/heater/{function}/{id}")
    public String forward(@PathVariable String function,@PathVariable long id) throws Exception{
        String url = Constants.getInformation_Service_URL("heater/" + function + "/" + id);
        String result =new HttpRequestor().doGet(url);
        return result;
    }
    @RequestMapping(value = "/instruction/{function}")
    public String forward2(@PathVariable String function,@RequestParam long userId) throws Exception{
        String url = Constants.getInformation_Service_URL("instruction/" + function + "?userId=" + userId);
        String result =new HttpRequestor().doGet(url);
        return result;
    }

    @RequestMapping(value = "/runBpmn",method = RequestMethod.POST)
    public String runBpmn(HttpServletRequest request) throws Exception{
        String bpmn = request.getParameter("bpmn");
        String temp = bpmn.split("process id=\"")[1];
        String processKey = temp.substring(0,temp.indexOf("\""));
        Map<String,String> params = new HashMap<>();
        params.put("userId",request.getParameter("userId"));
        params.put("processId",request.getParameter("processId"));
        params.put("bpmn",request.getParameter("bpmn"));
        String url;
        if(processKey.contains("GetCoffeeService")){
            params.put("workerId",request.getParameter("workerId"));
            params.put("taskId",request.getParameter("taskId"));
            url = Constants.getCSActiviti() + "/runBpmnEngine";
        }else{
            url = Constants.getActiviti()+ "/runBpmnEngine";
        }

        System.out.println(JSONObject.fromObject(params).toString());
        String result = new HttpRequestor().doPost(url,params);
        System.out.println(result);
        return "success";
    }

    @RequestMapping(value = "/getServiceDescription",method = RequestMethod.GET)
    public String getService(@RequestParam("serviceId") String serviceId) throws Exception{
        // 根据服务ID获取服务的描述
        String url = Constants.FIND_SERVICE_BY_NAME + "/" + serviceId;
        String serviceDescription = new HttpRequestor().doGet(url);
        System.out.println(serviceDescription);
        return serviceDescription;
    }

    @RequestMapping(value = "/getServiceDescription1")
    public String getService1(@RequestParam String serviceName) throws Exception{
        // 根据服务ID获取服务的描述
        String url = Constants.APP + "/" + serviceName;
        String serviceDescription = new HttpRequestor().doGet(url);
        System.out.println(serviceDescription);
        JSONObject jsonObject = JSONObject.fromObject(serviceDescription);

        return serviceDescription;
    }

    @RequestMapping(value = "/callService",method = RequestMethod.POST)
    public String callService(@RequestParam("serviceURL") String serviceURL,
                                       @RequestParam("requestMethod") String requestMethod,
                                       @RequestParam("serviceInputMap") String serviceInputMap) throws Exception{
        String result = "";
        if(requestMethod.equals("GET")){

            StringBuffer url = new StringBuffer(serviceURL);
            System.out.println(serviceInputMap);

            Map<String,String> inputMap = (Map<String, String>)JSONObject.fromObject(serviceInputMap);
            url.append("?");
            for (Map.Entry<String, String> entry : inputMap.entrySet())
                url.append(entry.getKey() + "=" + entry.getValue() + "&");
            if (url.substring(url.length()-1).equals("&"))
                url.delete(url.length()-1,url.length());
            System.out.println(url+"——————————————————————————————————");
            result = new HttpRequestor().doGet(url.toString());
        }else{
            Map<String,String> map = (Map<String, String>)JSONObject.fromObject(serviceInputMap);
            result = new HttpRequestor().doPost(serviceURL,map);
        }
        /*
        if(serviceURL.contains("Publish")){
            String data = JSONObject.fromObject(result).get("data").toString();
            String taskId = JSONObject.fromObject(data).get("simTaskId").toString();

        }
        */

        // 调用服务 得到返回结果

        System.out.println(new Date() + "-----" + result);
        return result;
    }
    @RequestMapping(value = "/callServicePost",method = RequestMethod.POST)
    public String callServicePost(@RequestParam("servicesURL") String serviceURL,
                                        @RequestParam("serviceInputMap") String serviceInputMap) throws Exception{
        String result = "";
        Map<String,String> params = new HashMap<>();
        params.put("servicesURL",serviceURL);
        params.put("serviceInputMap",serviceInputMap);
        result = new HttpRequestor().doPost(serviceURL,params);
        return result;
    }
    @RequestMapping(value = "/notifyApp")
    public void notifyApp(@RequestParam("processId") String processId,@RequestParam("nodeId") String nodeId,@RequestParam("userId") String userId) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("url","www.baidu.com");
        params.add("userId",userId);
        if(nodeId.contains("Start")){
            nodeId = "流程开始";
        }else if(nodeId.contains("End")){
            nodeId = "流程结束";
        }else if(nodeId.contains("TakeCup")){
            nodeId = "取杯子";
        }else if(nodeId.contains("TakeSugar")){
            nodeId = "取糖";
        }else if(nodeId.contains("TakeCoffee")){
            nodeId = "取咖啡";
        }else if(nodeId.contains("SendCoffee")){
            nodeId = "送咖啡";
        }else if(nodeId.contains("SelectCoffeeService")){
            nodeId = "选咖啡";
        }else if(nodeId.contains("GetRightCoffeeMachine")){
            nodeId = "选择合适咖啡机";
        }else if(nodeId.contains("GetCoffeeMachineStatus")){
            nodeId = "获取咖啡机状态";
        }else if(nodeId.contains("RunCoffeeMachine")){
            nodeId = "启动咖啡机";
        }else if(nodeId.contains("GetCoffeeMachineTime")){
            nodeId = "获取咖啡机时间";
        }else if(nodeId.contains("GetCoffeeCrowdSourcingService")){
            nodeId = "发起取咖啡众包";
        }
        if(nodeId.contains("2")&& !nodeId.contains("Control")){
            params.add("content", "当前节点执行到:" + nodeId + ".   位置: 401实验室" );
        }else {
            params.add("content", "当前节点执行到:" + nodeId);
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<String> result = restTemplate.postForEntity(Constants.APP, requestEntity, String.class);
            logger.info(String.format("the result of restTemplate is [ %s ]", result.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(new Date() + "当前执行到节点: "+nodeId);
    }
    @RequestMapping(value = "/notifyAppTime")
    public void notifyAppTime(@RequestParam("processId") String processId,@RequestParam("content") String content,@RequestParam("userId") String userId) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if(content.contains("isOver")){
            params.add("content", "咖啡已完成");
        }else{
            int time = Integer.valueOf(content);
            params.add("content", "剩余等待时间:" + time/60 + "分" + time%60 + "秒");
        }

            params.add("userId",userId);
        params.add("url","www.baidu.com");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<String> result = restTemplate.postForEntity(Constants.APP, requestEntity, String.class);
            logger.info(String.format("the result of restTemplate is [ %s ]", result.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(new Date() + "当前执行等待: "+content + "秒");
    }
    @RequestMapping(value = "/notifyAsyncTaskIsFinished",method = RequestMethod.GET)
    public String notifyAsync(@RequestParam("processInstanceId")String processInstanceId) throws Exception{
        new HttpRequestor().doGet(Constants.getActiviti() + "/notifyAsyncTaskIsFinished/" + processInstanceId);
        return "success";
    }
    @RequestMapping(value = "/completeTask",method = RequestMethod.POST)
    public String completeTask(@RequestParam("workerId") String workerId,
                               @RequestParam("taskId") String taskId)throws Exception{
        //MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        Map<String,String> map = new HashMap<>();
        //map.put("userId",workerId);
        map.put("taskId",taskId);
        //HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(map);
        //ResponseEntity<String> responseEntity = restTemplate.postForEntity(Constants.CS,requestEntity,String.class);
        String result = new HttpRequestor().doPost(Constants.CS,map);
        return result;
    }

}
