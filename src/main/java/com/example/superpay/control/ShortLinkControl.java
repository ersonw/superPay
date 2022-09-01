package com.example.superpay.control;

import com.example.superpay.service.ShortLinkService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ShortLinkControl {
    @Autowired
    private ShortLinkService service;
    @GetMapping("/s/{id}")
    public ModelAndView search(@PathVariable String id,
                               @RequestParam(value = "url",required = false) @ApiParam(hidden = true) String url ,
                               @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.search(id, url,ip);
    }
//    public void search(@PathVariable String id,
//                       @RequestParam(value = "url",required = false) @ApiParam(hidden = true) String url ,
//                       @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
//                       HttpServletResponse response){
//        service.search(id, url,ip,response);
//    }
}
