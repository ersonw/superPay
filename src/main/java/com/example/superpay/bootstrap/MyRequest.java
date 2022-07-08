package com.example.superpay.bootstrap;

import com.example.superpay.util.ToolsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class MyRequest extends HttpServletRequestWrapper {  //request
    private HttpServletRequest request;
    public MyRequest(HttpServletRequest request){
        super(request);
        this.request = request;
//        try {
//            request.setCharacterEncoding("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }
    @Override
    public String getParameter(String name) {
        String value = null;
        value = request.getParameter(name);
//        System.out.printf("%s\n",value);
//        try {
//            System.out.printf("%s\n",new String(value.getBytes(ToolsUtil.getEncoding(value)), StandardCharsets.UTF_8));
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.printf("%s\n",new String(value.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        return value;
    }
    //重构方法
    @Override
    public Map<String, String[]> getParameterMap() {
//        System.out.printf(ToolsUtil.getJsonBodyString(this.request));
//        return new HashMap();
        try{
            Map<String,String[]> map = this.request.getParameterMap();  //接受客户端的数据
            Map<String,String[]> newmap = new HashMap();
            for(Map.Entry<String, String[]> entry : map.entrySet()){
                String name = entry.getKey();
                String values[] = entry.getValue();

                if(values==null){
                    newmap.put(name, new String[]{});
                    continue;
                }
                String newvalues[] = new String[]{this.getParameter(name)};
                newmap.put(name, newvalues);
            }

            return newmap;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String[] getParameterValues(String name) {
// TODO Auto-generated method stub
        return super.getParameterValues(name);
    }
}
