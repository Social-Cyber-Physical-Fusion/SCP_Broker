package com.fdse.broker.controller;

import com.fdse.broker.constant.Constants;
import com.fdse.broker.http.HttpRequestor;
import net.sf.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HassController
 * @Description TODO
 * @Author LiuJJ
 * @Date 2019/1/15 9:01
 * @Version 1.0
 **/
@RestController("/hass")
public class HassController {
    @RequestMapping(value = "/getHassState")
    public String getState(@RequestParam("deviceId") String deviceId)throws Exception{
        //System.out.println(Constants.getHassState("states/light." + deviceId + "_state"));
        String states = new HttpRequestor().doGet(Constants.getHassState("/states/light." + deviceId + "_state"));
        JSONObject jsonObject = JSONObject.fromObject(states);
        return jsonObject.get("state").toString();
    }

    @RequestMapping(value = "/startCoffeeMachine",method = RequestMethod.POST)
    public String startCoffeeMachine(@RequestParam("mode") String mode,
                                     @RequestParam("level") String level,
                                     @RequestParam("num") String num,
                                     @RequestParam("deviceId") String deviceId) throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("action","start");
        param.put("mode",mode);
        param.put("level",level);
        param.put("num",num);
        param.put("deviceId",deviceId);
        JSONObject jsonObject = new JSONObject();
        Map<String,String> map = new HashMap<>();
        map.put("payload",JSONObject.fromObject(param).toString());
        map.put("topic",deviceId + "/switch");
        String request = JSONObject.fromObject(map).toString();
        new HttpRequestor().doPost(Constants.getHassState("/services/mqtt/publish"),request);
        System.out.println(request);
        return "success";
    }
    @RequestMapping(value = "/getRemainTime",method = RequestMethod.GET)
    public String getTime(@RequestParam("deviceId") String deviceId) throws Exception{
        String time = "";
        String entityId = "/states/sensor." + deviceId +"_time";
        String state = new HttpRequestor().doGet(Constants.getHassState(entityId));
        JSONObject jsonObject = JSONObject.fromObject(state);
        System.out.println(jsonObject);
        String attributes = jsonObject.get("attributes").toString();
        System.out.println(attributes);
        time = JSONObject.fromObject(attributes).get("LeftTime").toString();
        //time = jsonObject.getJSONObject("attributes").get("LeftTime").toString();
        return time;
    }
   //救命
    @RequestMapping(value = "/getCoffeeId",method = RequestMethod.GET)
    public String getCoffeeMachineId(String userId) throws Exception{
        String url = Constants.KG + "/get_available_coffee_maker/" + userId;
        String result = new HttpRequestor().doGet(url);
        String deviceId = JSONObject.fromObject(result).get("data").toString();
        return deviceId;
    }
    @RequestMapping(value = "/getCup",method = RequestMethod.GET)
    public String getCup(@RequestParam("userId") String userId) throws Exception{
        String url = Constants.KG + "/get_position_of_cup/" + userId;
        String result = new HttpRequestor().doGet(url);
        String loc = JSONObject.fromObject(result).get("position").toString();
        return loc;
    }

    @RequestMapping(value = "/getSugar",method = RequestMethod.GET)
    public String getSugar(@RequestParam("deviceId") String deviceId) throws Exception{
        String url = Constants.KG + "/get_position_of_sugar_container/" + deviceId;
        String result = new HttpRequestor().doGet(url);
        String loc = JSONObject.fromObject(result).get("position").toString();
        return loc;
    }

    @RequestMapping(value = "/getCoffee",method = RequestMethod.GET)
    public String getCoffee(@RequestParam("deviceId") String deviceId) throws Exception{
        String url = Constants.KG + "/get_position_of_coffee_maker/" + deviceId;
        String result = new HttpRequestor().doGet(url);
        String loc = JSONObject.fromObject(result).get("position").toString();
        return loc;
    }
}