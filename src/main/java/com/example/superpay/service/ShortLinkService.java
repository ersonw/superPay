package com.example.superpay.service;

import com.example.superpay.entity.ShortLink;
import com.example.superpay.repository.ShortLinkRepository;
import com.example.superpay.util.ToolsUtil;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class ShortLinkService {
    @Autowired
    private ShortLinkRepository shortLinkRepository;
//    public void search(String id, String url, String ip, HttpServletResponse response) {
//        System.out.printf("id:%s url:%s ip:%s\n",id,url,ip);
//        try {
//            if(StringUtils.isEmpty(id)) {
//                response.sendError(404);
//                return;
//            }
//            if(StringUtils.isEmpty(url)) {
//                response.sendError(404);
//                return;
//            }
//            if(StringUtils.isEmpty(ip)) {
//                response.sendError(404);
//                return;
//            }
//            ShortLink link = shortLinkRepository.findAllById(id);
////            System.out.println(link);
//            if(link == null) {
//                response.sendError(404);
//                return;
//            }
//            response.setHeader("referer",url);
//            response.sendRedirect(link.getLink());
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
    public ModelAndView search(String id, String url, String ip) {
//        System.out.printf("id:%s url:%s ip:%s\n",id,url,ip);
        try {
            if(StringUtils.isEmpty(id)) {
                throw new RuntimeException("id不存在");
            }
            if(StringUtils.isEmpty(url)) {
                throw new RuntimeException("url不存在");
            }
            if(StringUtils.isEmpty(ip)) {
                throw new RuntimeException("ip不存在");
            }
            ShortLink link = shortLinkRepository.findAllById(id);
//            System.out.println(link);
            if(link == null) {
                throw new RuntimeException("link不存在");
            }
            return ToolsUtil.getHtml(link.getLink());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ToolsUtil.errorHtml(e.getMessage());
        }
    }
}
