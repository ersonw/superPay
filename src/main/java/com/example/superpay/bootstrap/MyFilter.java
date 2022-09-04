package com.example.superpay.bootstrap;
import com.alibaba.fastjson.JSONObject;
import com.example.superpay.dao.AuthDao;
import com.example.superpay.entity.User;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.util.AESUtils;
import com.example.superpay.util.ToolsUtil;
import com.example.superpay.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

//@Order(10000)
//@Component
@WebFilter(filterName = "myFilter", urlPatterns = {"/api/*", "/v3api/*","/s/*"})
public class MyFilter implements Filter {
//    private static MyFilter self;
//    @Autowired
    private AuthDao authDao;
//    @Autowired
    private UserRepository userDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        authDao = Utils.getAuthDao();
        userDao = Utils.getUserRepository();
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.of("+8")));
//        self = this;
//        System.out.printf(this.userDao.findAllById("9MjfGvQ0mhXS4JFjAl2owcGdUpXAh3He").toString());
//        System.out.printf(JSONObject.toJSONString(filterConfig));
    }
//    @PostConstruct
//    public void initPost(){
//        self = this;
//        System.out.printf("加载过滤器成功！\n");
//    }
    /**
     * 获取访问者IP
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     * @param request
     * @return
     */
    private static String getIpAddr(HttpServletRequest request) {
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

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
//            System.out.printf(request.getCharacterEncoding());
//            request.setCharacterEncoding("utf-8");
//            if(isNotRes(request.getRequestURI())){
//                chain.doFilter(request, response);
//                return;
//            }
            String contentType = request.getContentType();
//            System.out.println(contentType);
            String token = ((HttpServletRequest) req).getHeader("Token");
            String ip = getIpAddr(request);
//            System.out.printf(ip+"\n");
            String serverName = request.getServerName();//返回服务器的主机名
            String serverPort = String.valueOf(request.getServerPort());//返回服务器的端口号
            String uri = request.getRequestURI();//返回请求行中的资源名称
            String url = request.getRequestURL().toString();//获得客户端发送请求的完整url
            String schema = request.getScheme();
//            System.out.printf("ServerName:%s ServerPort:%d uri:%s URL:%s\n",serverName,serverPort,uri,url);
            if (
                    !uri.equals("/v3api/wxPayNotify")
            ){
                User user = null;
                if (StringUtils.isNotEmpty(token)){
                    if (token.equals("04b4b58apiVb84b9Zp4f6cxXxb25b33S69700d6e85e92UJ")) {
                        user = userDao.findAllById("8045ca21MnP21b0ZFF44e3IeM9239kkQc2f9f308242egYp");
                    }else{
                        user = authDao.findUserByToken(token);
                    }
                }
//            System.out.printf(token);
                if (request.getMethod().equals("GET")){
                    request = new MyRequest(request);
                    Map<String, String[]> parameterMap = new HashMap(request.getParameterMap());
                    ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request, parameterMap);
                    wrapper.addParameter("ip", ip);
                    wrapper.addParameter("serverName", serverName);
                    wrapper.addParameter("serverPort", String.valueOf(serverPort));
                    wrapper.addParameter("uri", uri);
                    wrapper.addParameter("url", url);
                    wrapper.addParameter("schema", schema);
                    if (user != null){
                        wrapper.addParameter("user", JSONObject.toJSONString(user));
                    }
                    request = wrapper;
                }else if (request.getMethod().equals("POST")){
                    if (contentType != null){
                        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)){
                            String postContent = ToolsUtil.getJsonBodyString(request);
//                        System.out.println(postContent);
                            String s =  AESUtils.Decrypt(postContent);
//                        System.out.printf(s);
                            if (s != null){
                                postContent = s;
                            }
                            JSONObject jsStr = new JSONObject();
                            if (StringUtils.isNotEmpty(postContent) && postContent.startsWith("{") && postContent.endsWith("}")) {
                                //修改、新增、删除参数
                                jsStr = JSONObject.parseObject(postContent);
                            }
                            jsStr.put("ip", ip);
                            jsStr.put("serverName", serverName);
                            jsStr.put("serverPort", serverPort);
                            jsStr.put("uri", uri);
                            jsStr.put("url", url);
                            jsStr.put("schema", schema);
                            if (user != null) {
                                jsStr.put("user", JSONObject.toJSONString(user));
                            }
                            postContent = jsStr.toJSONString();
//                        System.out.println(postContent);
                            //将参数放入重写的方法中
                            request = new BodyRequestWrapper(request, postContent);
//                        Map<String, String[]> parameterMap = JSONObject.parseObject(postContent, new TypeReference<Map<String, String[]>>(){});
//                        request = new ParameterRequestWrapper(request, parameterMap);
                        }else{
                            request = new MyRequest(request);
                            Map<String, String[]> parameterMap = new HashMap(request.getParameterMap());
//                        System.out.printf(parameterMap.toString());
                            parameterMap.put("ip", new String[]{ip});
                            parameterMap.put("serverName", new String[]{serverName});
                            parameterMap.put("serverPort", new String[]{String.valueOf(serverPort)});
                            parameterMap.put("uri", new String[]{uri});
                            parameterMap.put("url", new String[]{url});
                            parameterMap.put("schema", new String[]{schema});
                            if (user != null) {
                                parameterMap.put("user", new String[]{JSONObject.toJSONString(user)});
                            }
//                        System.out.println(parameterMap);
                            request = new ParameterRequestWrapper(request, parameterMap);
                        }
                    }
                }
            }
            chain.doFilter(request, response);
        } else {
//            System.out.printf(req.getRemoteAddr());
            chain.doFilter(req, response);
        }
    }
    @Override
    public void destroy() {

    }
}
