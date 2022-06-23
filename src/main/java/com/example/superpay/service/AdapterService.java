package com.example.superpay.service;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.EPayData;
import com.example.superpay.data.ResponseData;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.util.ToolsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdapterService {
    @Autowired
    private UserRepository userRepository;

    public ResponseData ePayOrder(EPayData ePay) {
        if (StringUtils.isEmpty(ePay.getPid())) return ResponseData.error("Invalid  Parent ID");
        return ResponseData.success();
    }
}
