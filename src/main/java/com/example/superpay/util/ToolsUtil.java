package com.example.superpay.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.RequestHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
@Slf4j
public class ToolsUtil {
    private static ToolsUtil self;
    public static final int TIME_OUT = 30;
    public static final int MAX_Black = 3;
    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', };
    public static boolean isCorrectIp(String ipString) {
        //1、判断是否是7-15位之间（0.0.0.0-255.255.255.255.255）
        if (ipString.length()<7||ipString.length()>15) {
            return false;
        }
        //2、判断是否能以小数点分成四段
        String[] ipArray = ipString.split("\\.");
        if (ipArray.length != 4) {
            return false;
        }
        for (int i = 0; i < ipArray.length; i++) {
            //3、判断每段是否都是数字
            try {
                int number = Integer.parseInt(ipArray[i]);
                //4.判断每段数字是否都在0-255之间
                if (number <0||number>255) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    public static boolean isCorrectIp2(String ipString) {
        String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";	//IP地址的正则表达式
        //如果前三项判断都满足，就判断每段数字是否都位于0-255之间
        if (ipString.matches(ipRegex)) {
            String[] ipArray = ipString.split("\\.");
            for (int i = 0; i < ipArray.length; i++) {
                int number = Integer.parseInt(ipArray[i]);
                //4.判断每段数字是否都在0-255之间
                if (number <0||number>255) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;	//如果与正则表达式不匹配，则返回false
        }
    }
    public static Double getMoney(double d){
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static ModelAndView errorHtml(String msg){
        ModelAndView error = new ModelAndView("payHtml/error");
        error.addObject("msg",msg);
        return error;
    }
    public static ModelAndView postHtml(String url, JSONObject params){
        ModelAndView post = new ModelAndView("payHtml/post");
        post.addObject("url",url);
        post.addObject("params",params);
        return post;
    }
    public static ModelAndView getHtml(String url){
        ModelAndView post = new ModelAndView("payHtml/get");
        post.addObject("url",url);
        return post;
    }
    public static ModelAndView emptyHtml(String data){
        ModelAndView post = new ModelAndView("payHtml/empty");
        post.addObject("data",data);
        return post;
    }
    public static ModelAndView waitHtml(){
        ModelAndView post = new ModelAndView("payHtml/wait");
//        post.addObject("url",url);
        return post;
    }
    public static ModelAndView refreshHtml(Map<String, ?> modelMap){
        ModelAndView post = new ModelAndView("payHtml/refresh");
        post.addAllObjects(modelMap);
        return post;
    }
    public static ModelAndView wxpayHtml(Map<String, ?> modelMap){
        ModelAndView post = new ModelAndView("payHtml/wxpay");
        post.addAllObjects(modelMap);
        return post;
    }
    public static ModelAndView alipayHtml(Map<String, ?> modelMap){
        ModelAndView post = new ModelAndView("payHtml/alipay");
        post.addAllObjects(modelMap);
        return post;
    }
    public static ModelAndView waitHtml(Map<String, ?> modelMap){
        ModelAndView post = new ModelAndView("payHtml/wait_h5");
        post.addAllObjects(modelMap);
        return post;
    }
    private static boolean isNotRes(String str){
        List<String> parts = new ArrayList<>();
        parts.add(".js");
        parts.add(".css");
        parts.add(".html");
        parts.add(".txt");
        parts.add("jpg");
        parts.add(".png");
        parts.add(".gif");
        parts.add(".svg");
        parts.add(".ico");
        parts.add(".jpeg");
        parts.add(".woff2");
        parts.add(".map");
        for (String s: parts) {
            if(str.endsWith(s)){
                return true;
            }
        }
//        System.out.printf(str);
//        System.out.printf("\n");
        return false;
    }
    @PostConstruct
    public void init(){
        self = this;
    }
    //首字母转小写
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    public static long cardinality(long max){
        return cardinality(100,max);
    }
    public static long cardinality(long mini, long max){
        if (mini < 0) mini = 0;
        if (max < mini) max = mini+2;
        return  (long) (mini+Math.random()*(max-mini+1));
    }
    public static boolean checkEmailFormat(String content){
        String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
        Pattern p = Pattern.compile(REGEX);
        Matcher matcher=p.matcher(content);

        return matcher.matches();
    }
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if(ip.contains("../")||ip.contains("..\\")){
                return "";
            }
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                ip= ip.substring(0, index);
            }
            if(ip.contains("../")||ip.contains("..\\")){
                return "";
            }
            return ip;
        } else {
            ip=request.getRemoteAddr();
            if(ip.contains("../")||ip.contains("..\\")){
                return "";
            }
            if(ip.equals("0:0:0:0:0:0:0:1")){
                ip="127.0.0.1";
            }
            return ip;
        }

    }
    public static <T> T getRequestBody(HttpServletRequest request,  Class<T> clazz){
        String s = getRequestBody(request);
        if (StringUtils.isNotEmpty(s)){
            if (s.startsWith("[") && s.endsWith("]")){
                return JSONArray.parseObject(s, clazz);
            }else {
                return JSONObject.parseObject(s, clazz);
            }
        }
        return null;
    }
    public static String getRequestBody(HttpServletRequest request){
        return getJsonBodyString(request);
    }
    public static RequestHeader getRequestHeaders(HttpServletRequest request){
        RequestHeader data = new RequestHeader();
        data.setIp(getIpAddr(request));
        data.setUserAgent(request.getHeader("User-Agent"));
        data.setServerName(request.getServerName());
        data.setServerPort(request.getServerPort());
        data.setUri(request.getRequestURI());
        data.setUrl(request.getRequestURL().toString());
        data.setSchema(request.getScheme());
        data.setQuery(request.getQueryString());
        data.setReferer(request.getHeader("referer"));
        data.setUser(request.getHeader("user"));
        data.setClient(request.getHeader("client"));
        data.setOpenId(request.getHeader("X-WX-OPENID"));
        data.setAppId(request.getHeader("X-WX-APPID"));
        data.setUnionId(request.getHeader("X-WX-UNIONID"));
        data.setFromOpenId(request.getHeader("X-WX-FROM-OPENID"));
        data.setFromAppId(request.getHeader("X-WX-FROM-APPID"));
        data.setFromUnionId(request.getHeader("X-WX-FROM-UNIONID"));
        data.setWxEnv(request.getHeader("X-WX-ENV"));
        data.setWxSource(request.getHeader("X-WX-SOURCE"));
        data.setForwardedFor(request.getHeader("X-Forwarded-For"));
        return data;
    }
    public static String getJsonBodyString(HttpServletRequest httpServletRequest) {
        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static  String getToken(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","")+System.currentTimeMillis();
    }
    public static String getRandom(int n){
        return RandomStringUtils.randomAlphanumeric(n);
    }
    public static String getSalt(){
        return getRandom(32);
    }
    public  static boolean isNumberString(String s){
        for (int i=0;i< s.length(); i++){
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }
    public static String getWxSign(Map<String, String> map, String key) {

        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            // 构造签名键值对的格式
            List<String> values = new ArrayList<String>();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String k = item.getKey();
                    String v = item.getValue();
                    if (!(v == "" || v == null)) {
                        values.add(k + "=" + v);
                    }
                }
            }

            String sign = StringUtils.join(values, "&") + key;
            //System.out.println(sign);

            //进行MD5加密
            result = DigestUtils.md5DigestAsHex(sign.getBytes()).toUpperCase();
        } catch (Exception e) {
            return null;
        }
        return result;
    }
    public  static String doPost(String url, Map<String, String> map) throws Exception {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");
            RequestConfig build = RequestConfig.custom().setConnectTimeout(10000).build();
            httpPost.setConfig(build);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception ex) {
            log.error("Error doPost {}\nurl: {} \n {}",ex.getMessage(),url,JSONObject.toJSONString(map));
//            ex.printStackTrace();
            //throw new Exception();
        }
        return result;
    }
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = reader.readLine();
            if (strRead != null) {
                sbf.append(strRead);
                while ((strRead = reader.readLine()) != null) {
                    sbf.append("\n");
                    sbf.append(strRead);
                }
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null)
            return str;
        try {
            strret = URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
    }
    public static String asHex(byte hash[]) {
        char buf[] = new char[hash.length * 2];
        for (int i = 0, x = 0; i < hash.length; i++) {
            buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
            buf[x++] = HEX_CHARS[hash[i] & 0xf];
        }
        return new String(buf);
    }
    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
    public static String md5PHP(String str) {
        try {
            byte[] bytesOfMessage = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            return asHex(thedigest);
        }catch (Exception e) {
            log.info("md5PHP {}", e.getMessage());
            return null;
        }
    }
    public static String md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
    public static String encrypt(String key, String src){
        String p = null;
//        String src = "name=Alice&text=Hello";
        byte[] aesKey = key.getBytes(StandardCharsets.UTF_8);
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"));
            byte[] encrypted = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            p = Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            e.printStackTrace();
        }
        return p;
    }
    public static String getSign(String key, String p, int t){
        return DigestUtils.md5DigestAsHex((p + t + key).getBytes());
    }
    public static String sendGet(String httpUrl, Map<String, String> parameter) {
        if (parameter == null || httpUrl == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = parameter.entrySet().iterator();
        while (iterator.hasNext()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value;
            try {
                value = URLEncoder.encode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                value = "";
            }
            sb.append(key).append('=').append(value);
        }
        String urlStr = null;
        if (httpUrl.lastIndexOf('?') != -1) {
            urlStr = httpUrl + '&' + sb.toString();
        } else {
            urlStr = httpUrl + '?' + sb.toString();
        }

        HttpURLConnection httpCon = null;
        String responseBody = null;
        try {
            URL url = new URL(urlStr);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("GET");
            httpCon.setConnectTimeout(TIME_OUT * 1000);
            httpCon.setReadTimeout(TIME_OUT * 1000);
            // 开始读取返回的内容
            InputStream in = httpCon.getInputStream();
            byte[] readByte = new byte[1024];
            // 读取返回的内容
            int readCount = in.read(readByte, 0, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (readCount != -1) {
                baos.write(readByte, 0, readCount);
                readCount = in.read(readByte, 0, 1024);
            }
            responseBody = new String(baos.toByteArray(), "UTF-8");
            baos.close();
        } catch (Exception ignored) {
        } finally {
            if (httpCon != null)
                httpCon.disconnect();
        }
        return responseBody;
    }
}
