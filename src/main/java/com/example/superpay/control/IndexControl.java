package com.example.superpay.control;

import com.example.superpay.util.ToolsUtil;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RestController
public class IndexControl{
    @GetMapping(value = "/")
    public String index(){
//        System.out.printf(getClassPath());
        return "index";
    }
    public String getClassPath(){
        return getClassPath("/");
    }
    public String getClassPath(String path){
        return this.getClass().getClassLoader().getResource(path).getPath();
    }
}