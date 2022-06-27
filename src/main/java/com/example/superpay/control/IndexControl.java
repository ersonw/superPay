package com.example.superpay.control;

import com.example.superpay.util.ToolsUtil;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@RestController
public class IndexControl{
    @GetMapping(value = "/")
    public String index(){
        return "index";
    }
    @GetMapping(value = "/admin")
    public ModelAndView admin1(){
        return ToolsUtil.postHtml("/admin/index.html");
    }
    @GetMapping(value = "/admin/")
    public ModelAndView admin(){
        return ToolsUtil.postHtml("/admin/index.html");
    }
    @GetMapping(value = "/admin/index")
    public ModelAndView adminIndex(){
        return ToolsUtil.postHtml("/admin/index.html");
    }
    public String getClassPath(){
        return getClassPath("/");
    }
    public String getClassPath(String path){
        return this.getClass().getClassLoader().getResource(path).getPath();
    }
}