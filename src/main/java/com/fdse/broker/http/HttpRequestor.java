package com.fdse.broker.http;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName HttpRequestor
 * @Description TODO
 * @Author LiuJJ
 * @Date 2019/1/10 10:13
 * @Version 1.0
 **/
public class HttpRequestor {
    private String charset = "utf-8";
    private Integer connectTimeout = null;
    private Integer socketTimeout = null;
    private String proxyHost = null;
    private Integer proxyPort = null;

    /**
     * Do GET request
     * @param url
     * @return
     * @throws Exception
     * @throws IOException
     */
    public String doGet(String url) throws Exception {

        URL localURL = new URL(url);


        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("X-HA-Access", "welcome");

        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setConnectTimeout(300*1000);

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }

        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } finally {

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer.toString();
    }

    /**
     * Do POST request
     * @param url
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public String doPost(String url, Map parameterMap) throws Exception {

        /* Translate parameter map to parameter date string */
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String)iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String)parameterMap.get(key);
                } else {
                    value = "";
                }

                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }

        System.out.println("POST parameter : " + parameterBuffer.toString());

        URL localURL = new URL(url);

        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setConnectTimeout(300*1000);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
//        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));

        OutputStream outputStream = null;

        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {

            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write(parameterBuffer.toString());
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {

                resultBuffer.append(tempLine);
            }

        } finally {

            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer.toString();
    }
    public String doPost(String url, String parameter) throws Exception {

        URL localURL = new URL(url);

        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("X-HA-Access", "welcome");
        httpURLConnection.setConnectTimeout(300*1000);
        //httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameter.length()));

        OutputStream outputStream = null;

        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {

            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write(parameter);
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {

                resultBuffer.append(tempLine);
            }

        } finally {

            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer.toString();
    }

    private URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }

    /**
     * Render request according setting
     * @param
     */
    private void renderRequest(URLConnection connection) {

        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }

        if (socketTimeout != null) {
            connection.setReadTimeout(socketTimeout);
        }

    }

    /*
     * Getter & Setter
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public static void main(String[] args) throws Exception{
        /* Post Request */
//        Map dataMap = new HashMap();
//        dataMap.put("username", "Nick Huang");
//        dataMap.put("blog", "IT");
//        System.out.println(new HttpRequestor().doPost("http://localhost:8080/OneHttpServer/", dataMap));

        /* Get Request */
//        System.out.println(new HttpRequestor().doGet("http://47.100.23.182:10037/repositories/OntologyTest/statements" +
//                "?subj=%3Chttp://www.semanticweb.org/fudan/ontologies/Human_Machine_Thing%23IfRoomEmptyService_Grounding%3E" +
//                "&pred=%3Chttp://www.semanticweb.org/fudan/ontologies/Human_Machine_Thing%23HmtServiceEntranceURL%3E"));
//        System.out.println(new HttpRequestor().doGet("http://47.100.23.182:10037/repositories/OntologyTest?query=SELECT%20%3Fp%20%3Fo%20%7B%20%3Chttp%3A%2F%2Fwww.semanticweb.org%2Ffudan%2Fontologies%2FHuman_Machine_Thing%23IfRoomEmptyService%3E%20%3Fp%20%3Fo%7D"));

//        System.out.println(new HttpRequestor().doGet("http://47.100.23.182:8004/query/compositeServiceQuery?serviceName=IfRoomEmptyService"));
//        HashMap<String,String> map=new HashMap<String,String>();
////        map.put("queueUserId","4");
//        map.put("userId","5");
//        map.put("commodityId","2");
//        String result = new HttpRequestor().doPost("http://192.168.1.142:8080/queue/entry",map);
//        JSONObject jb = JSONObject.fromObject(result);
//        Map<String, String> resultMap = (Map<String, String>)jb;
//        System.out.println(resultMap.toString());
////        System.out.println(new HttpRequestor().doPost("http://192.168.1.142:8080/queue/entry",map));

        String result = new HttpRequestor().doGet("http://47.100.23.182:8004/query/serviceParamQuery?serviceName=GetFirstPersonInQueueService");
        JSONObject jb = JSONObject.fromObject(result);
//        Map<String, String> resultMap = (Map<String, String>)jb;
//        System.out.println(resultMap.toString());
        String serviceURL = jb.get("serviceURL").toString();
        String serviceType = jb.get("serviceType").toString();
        JSONObject serviceInput = JSONObject.fromObject(jb.get("serviceInput"));
        JSONObject serviceOutput = JSONObject.fromObject(jb.get("serviceOutput"));

        Map<String, String> serviceInputMap = (Map<String, String>)serviceInput;
        StringBuffer url = new StringBuffer(serviceURL);
        url.append("?");
        for (Map.Entry<String, String> entry : serviceInputMap.entrySet())
            url.append(entry.getKey() + "=" + serviceInputMap.get(entry.getKey()) + "&");
        if (url.substring(url.length()-1).equals("&"))
            url.delete(url.length()-1,url.length());
        System.out.println(url);
    }
}
