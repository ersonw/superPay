package com.example.superpay.service;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.*;
import com.example.superpay.entity.*;
import com.example.superpay.repository.*;
import com.example.superpay.util.*;
import com.github.wxpay.sdk.WXPayUtil;
import io.netty.handler.codec.base64.Base64Encoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdapterService {
    private static int PAY_TYPE_INDEX = 0;
    private static final int PAY_TYPE_DEFAULT = 0;
    private static final int PAY_TYPE_NATIVE = 1;
    private static final int PAY_TYPE_EPAY = 2;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PayTypeRepository payTypeRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private ShortLinkRepository shortLinkRepository;

    public ModelAndView ePayOrder(EPayData ePay) {
//        System.out.printf(JSONObject.toJSONString(ePay));
        if (ePay.getPid() == 0) {
            return ToolsUtil.errorHtml("商户ID不为空!");
        }
        User user = userRepository.findAllByPid(ePay.getPid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户ID不存在!");
        }
        if (user.getState() != 0) {
            return ToolsUtil.errorHtml("商户存在异常，请联系管理员!");
        }
        if (!ePay.isSign(user.getSecretKey())) {
            return ToolsUtil.errorHtml("数据效验失败!");
        }
        Order order = orderRepository.findAllByOutTradeNo(ePay.getOut_trade_no());
        if (order != null) {
            if (order.getState() == 1) {
                return ToolsUtil.errorHtml("商户订单已重复!");
            } else {
                return ToolsUtil.getHtml("/v3api/callback?outTradeNo=" + order.getOutTradeNo());
            }
        }
        PayType type = payTypeRepository.findAllByType(ePay.getType());
        if (type == null) {
            return ToolsUtil.errorHtml("支付方式已被禁用!");
        }
        ThirdParty party = getThird(type.getId());
        if (party == null) {
            return ToolsUtil.errorHtml("通道已关闭！");
        }
//        Order order = new Order();
        order = new Order();
        order.setUid(user.getId());
        order.setOutTradeNo(ePay.getOut_trade_no());
        order.setThirdPartyId(party.getId());
        order.setIp(ePay.getIp());

        if (StringUtils.isNotEmpty(ePay.getReturn_url())) {
            order.setReturnUrl(ePay.getReturn_url());
        } else if (StringUtils.isNotEmpty(user.getCallbackUrl())) {
            order.setReturnUrl(user.getCallbackUrl());
        } else {
            return ToolsUtil.errorHtml("同步通知地址不可为空！");
        }
        if (StringUtils.isNotEmpty(ePay.getNotify_url())) {
            order.setNotifyUrl(ePay.getNotify_url());
        } else if (StringUtils.isNotEmpty(user.getNotifyUrl())) {
            order.setNotifyUrl(user.getNotifyUrl());
        } else {
            return ToolsUtil.errorHtml("异步通知地址不可为空！");
        }
        if (!order.getReturnUrl().startsWith("http") || !order.getNotifyUrl().startsWith("http"))
            return ToolsUtil.errorHtml("通知回调地址不可为空！");
        order.setMoney(new BigDecimal(ePay.getMoney()).doubleValue());
//        order.setName(ePay.getName());
        order.setAddTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());

        return handlerThirdParty(party, order, ePay.getType(),ePay.getIp());
    }

    private ModelAndView handlerThirdParty(ThirdParty thirdParty, Order order, String type,String ip) {
        String domain = thirdParty.getDomain();
        if (StringUtils.isNotEmpty(domain) && !domain.startsWith("http")){
            domain = "http://" + domain;
        }
        switch (thirdParty.getThird()) {
            case PAY_TYPE_DEFAULT:
                String url = ThirdPartyUtil.toPay(order, thirdParty);
                if (url != null) {
                    orderRepository.save(order);
                    return ToolsUtil.getHtml(url);
                }
                break;
            case PAY_TYPE_NATIVE:
                if (type.equals("wxpay")){
                    String wxUrl = WxPayUtil.wxPayH5s(thirdParty,order,ip);
                    if (wxUrl != null) {
                        orderRepository.save(order);
                        ShortLink wxLink = new ShortLink(wxUrl);
                        shortLinkRepository.save(wxLink);
                        return ToolsUtil.getHtml(domain+"/s/"+wxLink.getId());
                    }
                }else if (type.equals("alipay")){
                    String data = AlipayUtil.alipay(thirdParty,order);
                    if (data != null) {
                        orderRepository.save(order);
//                        ShortLink aliLink = new ShortLink(data);
//                        shortLinkRepository.save(aliLink);
//                        return ToolsUtil.getHtml(domain+"/s/"+aliLink.getId());
//                        System.out.println(data);
                        return ToolsUtil.emptyHtml(data);
                    }
                }
                break;
            case PAY_TYPE_EPAY:
                String ePayData = EPayUtil.submit(thirdParty,order,type);
                if (ePayData != null) {
                    orderRepository.save(order);
//                    System.out.println(ePayData);
                    return ToolsUtil.emptyHtml(ePayData);
                }
                break;
            default:
                break;
        }
        return ToolsUtil.errorHtml("未获取到第三方资源！");
    }

    private ThirdParty getThird(String type) {
        Pageable pageable = PageRequest.of(PAY_TYPE_INDEX, 1);
        Page<ThirdParty> parties = thirdPartyRepository.findAllByTypeIdAndState(type, 1, pageable);
        if (parties.getTotalPages() < PAY_TYPE_INDEX || parties.getTotalPages() == PAY_TYPE_INDEX) {
            PAY_TYPE_INDEX = 0;
            pageable = PageRequest.of(PAY_TYPE_INDEX, 1);
            parties = thirdPartyRepository.findAllByTypeIdAndState(type, 1, pageable);
        }
        if (parties.getTotalPages() > PAY_TYPE_INDEX) {
            PAY_TYPE_INDEX++;
        }
        if (parties.getTotalPages() < PAY_TYPE_INDEX || parties.getTotalPages() == PAY_TYPE_INDEX) {
            PAY_TYPE_INDEX = 0;
        }
        if (parties.getContent().size() == 0) return null;
        return parties.getContent().get(0);
    }

    public EPayData test() {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setThird(1);
        thirdParty.setAddTime(System.currentTimeMillis());
        thirdParty.setState(1);
        thirdParty.setTitle("易支付宝");
        thirdParty.setMchId("202206252104000");
        thirdParty.setSecretKey("3gOVsdBIgJdDSvOVhd8IlNgSMv43yfEk");
//        thirdParty.setAppid("wx4fcd04bc73de65e1");
        thirdParty.setNotifyUrl("https://pay.telebott.com/v3api/ePayNotify");
        thirdParty.setCallbackUrl("https://pay.telebott.com/v3api/ePayReturn");
        thirdParty.setTypeId("de12ad07CsHdf1ekeu4965KrRb08ec9F89088fa05746ciI");
        thirdParty.setRate(0.1);
//        try{
//            String certPath = WXConfigUtil.class.getClassLoader().getResource("").getPath();
//            File file = new File(certPath+ "apiclient_cert.p12");
//            InputStream certStream = new FileInputStream(file);
//            byte[] certData = new byte[(int) file.length()];
//            certStream.read(certData);
//            certStream.close();
//            String certStr = Base64.getEncoder().encodeToString(certData);
//            thirdParty.setPublicKey(certStr);
//            thirdPartyRepository.save(thirdParty);
////            System.out.println(certStr);
////            byte[] temp = Base64.getDecoder().decode(certStr);
////            System.out.printf("data: %d === temp: %d\n",certData.length,temp.length);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        thirdPartyRepository.save(thirdParty);
//        PayType type = new PayType();
//        type.setType("alipay");
//        type.setAddTime(System.currentTimeMillis());
//        type.setUpdateTime(System.currentTimeMillis());
//        type.setName("支付宝");
//        payTypeRepository.save(type);
        EPayData data = new EPayData();
        data.setPid(202206252104000L);
        data.setType("alipay");
        data.setOut_trade_no(TimeUtil._getOrderNo());
        data.setNotify_url("https://pay.telebott.com/v3api/testNotify");
        data.setReturn_url("https://pay.telebott.com/v3api/testReturn");
        data.setMoney("1.00");
        data.setSign(data.getSign("3gOVsdBIgJdDSvOVhd8IlNgSMv43yfEk"));
        data.setIp("127.0.0.1");
        return data;
    }

    public ModelAndView sError() {
        return ToolsUtil.errorHtml("支付失败");
    }

    public ModelAndView sCallback(String out_trade_no, String ip) {
        System.out.printf("test out_trade_no:" + out_trade_no + " ip:" + ip + "\n");
        Order order = orderRepository.findAllByOutTradeNo(out_trade_no);
        if (order == null) {
            return ToolsUtil.errorHtml("订单号不存在!");
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户不存在!");
        }
        if (order.getState() == 0) {
            return ToolsUtil.waitHtml();
        }
        String url = order.getReturnUrl();
        EPayNotify notify = getEPayNotify(order, user);
        try {
            URL u = new URL(url);
            if (StringUtils.isNotEmpty(u.getQuery())) {
                url = url + "&";
            } else {
                url = url + "?";
            }
            return ToolsUtil.getHtml(url + EPayNotify.getParameter(notify));
        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return ToolsUtil.errorHtml("商户回调地址错误!");
    }

    public String sNotify(ToPayNotify toPayNotify) {
        System.out.printf("toPayNotify:", toPayNotify + "\n");
        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(toPayNotify.getMchid());
        if (partys.isEmpty()) {
            return "error";
        }
        ThirdParty party = partys.get(0);
        if (!ThirdPartyUtil.toPayVerify(toPayNotify, party)) {
            return "error";
        }
        Order order = orderRepository.findAllByOutTradeNo(toPayNotify.getOut_trade_no());
        if (order == null) {
            return "error";
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return "error";
        }
        if (order.getState() == 1) {
            return "success";
        }
        order.setUpdateTime(System.currentTimeMillis());
        order.setState(1);
        order.setNotifyState(0);
        order.setTradeNo(toPayNotify.getTrade_no());
        order.setTotalFee(new BigDecimal(toPayNotify.getTotal_fee()).doubleValue());
        double fee = 0D;
        Fee f = new Fee();
        f.setAddTime(System.currentTimeMillis());
        f.setUid(user.getId());
        f.setOrderId(order.getId());
        f.setTip("第三方通道手续费");
        /**
         * 扣除通道费
         */
        if (party.getRate() > 0) {
            Double rate = party.getRate();
            Double rateFee = 0D;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                } else {
                    rateFee = order.getTotalFee();
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
            }
            fee = ToolsUtil.getMoney(rateFee);
            f.setPartyFee(rateFee);
        }
        /**
         * 扣除自定义费率
         */
        if (user.getRate() > 0) {
            Double rate = user.getRate();
            Double rateFee = 0D;
            Double totalFee = order.getTotalFee() - fee;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                } else {
                    rateFee = order.getTotalFee();
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
                if (rateFee > totalFee) {
                    rateFee = totalFee;
                }
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setRateFee(ToolsUtil.getMoney(rateFee));
        }
        /**
         * 扣除单笔手续费
         */
        if (user.getFee() > 0) {
            Double totalFee = order.getTotalFee() - fee;
            Double rateFee = 0D;
            if (user.getFee() > totalFee) {
                rateFee = totalFee;
            } else {
                rateFee = user.getFee();
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setSelfFee(ToolsUtil.getMoney(rateFee));
        }
        order.setFee(fee);
        orderRepository.save(order);
        if (f.isNotEmpty()) {
            mongoTemplate.save(f);
        }
        handlerThirdPartyNotify(order);
        return "success";
    }

    private String getPayType(String thirdId) {
        ThirdParty thirdParty = thirdPartyRepository.findAllById(thirdId);
        if (thirdParty != null) {
            PayType type = payTypeRepository.findAllById(thirdParty.getTypeId());
            if (type != null) {
                return type.getType();
            }
        }
        return "unknown";
    }

    public EPayNotify getEPayNotify(Order order, User user) {

        EPayNotify notify = new EPayNotify();
        notify.setPid(user.getPid());
        notify.setTrade_no(order.getTradeNo());
        notify.setOut_trade_no(order.getOutTradeNo());
        notify.setType(getPayType(order.getThirdPartyId()));
        notify.setMoney(String.valueOf(order.getMoney()));
        notify.setTrade_status(order.getState() == 1 ? "TRADE_SUCCESS" : "TRADE_FAILED");
        if (StringUtils.isNotEmpty(order.getName())) {
            notify.setName(order.getName());
        }
        notify.setSign(notify.getSign(user.getSecretKey()));
//        System.out.printf(notify+"\n");
        return notify;
    }

    private void handlerThirdPartyNotify(Order order) {
        User user = userRepository.findAllById(order.getUid());
        if (user == null) return;
        String url = order.getNotifyUrl();
        EPayNotify notify = getEPayNotify(order, user);
        if (ToolsUtil.request(url, EPayNotify.getParameter(notify)).equals("success")) {
            order.setNotifyState(1);
        }
        orderRepository.save(order);
    }

    public String ePayNotify(EPayNotify ePayNotify) {
        System.out.printf("%s\n", ePayNotify);
        if (ePayNotify.getPid() == 0) return "error";
        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(ePayNotify.getPid().toString());
        if (partys.isEmpty()) {
            return "error";
        }
        ThirdParty party = partys.get(0);
        if(!ePayNotify.isSign(party.getSecretKey())) return "error";
        Order order = orderRepository.findAllByOutTradeNo(ePayNotify.getOut_trade_no());
        if (order == null) {
            return "error";
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return "error";
        }
        if (order.getState() == 1) {
            return "success";
        }
        order.setUpdateTime(System.currentTimeMillis());
        order.setState(1);
        order.setNotifyState(0);
        order.setTradeNo(ePayNotify.getTrade_no());
        order.setTotalFee(new BigDecimal(String.valueOf(ePayNotify.getMoney())).doubleValue());
        double fee = 0D;
        Fee f = new Fee();
        f.setAddTime(System.currentTimeMillis());
        f.setUid(user.getId());
        f.setOrderId(order.getId());
        f.setTip("第三方通道手续费");
        /**
         * 扣除通道费
         */
        if (party.getRate() > 0) {
            Double rate = party.getRate();
            Double rateFee = 0D;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                } else {
                    rateFee = order.getTotalFee();
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
            }
            fee = ToolsUtil.getMoney(rateFee);
            f.setPartyFee(rateFee);
        }
        /**
         * 扣除自定义费率
         */
        if (user.getRate() > 0) {
            Double rate = user.getRate();
            Double rateFee = 0D;
            Double totalFee = order.getTotalFee() - fee;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                } else {
                    rateFee = order.getTotalFee();
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
                if (rateFee > totalFee) {
                    rateFee = totalFee;
                }
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setRateFee(ToolsUtil.getMoney(rateFee));
        }
        /**
         * 扣除单笔手续费
         */
        if (user.getFee() > 0) {
            Double totalFee = order.getTotalFee() - fee;
            Double rateFee = 0D;
            if (user.getFee() > totalFee) {
                rateFee = totalFee;
            } else {
                rateFee = user.getFee();
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setSelfFee(ToolsUtil.getMoney(rateFee));
        }
        order.setFee(fee);
        orderRepository.save(order);
        if (f.isNotEmpty()) {
            mongoTemplate.save(f);
        }
        handlerThirdPartyNotify(order);
        return "success";
    }
    public ModelAndView ePayReturn(EPayNotify ePayNotify) {
        if (ePayNotify.getPid() == 0) return ToolsUtil.errorHtml("商户不存在!");
        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(ePayNotify.getPid().toString());
        if(partys.size() == 0) return ToolsUtil.errorHtml("商户不存在！");
        ThirdParty party = partys.get(0);
        if(!ePayNotify.isSign(party.getSecretKey())) return ToolsUtil.errorHtml("效验失败");
        Order order = orderRepository.findAllByOutTradeNo(ePayNotify.getOut_trade_no());
        if (order == null) {
            return ToolsUtil.errorHtml("订单号不存在!");
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户不存在!");
        }
        if (order.getState() == 0) {
            return ToolsUtil.waitHtml();
        }
        String url = order.getReturnUrl();
        EPayNotify notify = getEPayNotify(order, user);
        try {
            URL u = new URL(url);
            if (StringUtils.isNotEmpty(u.getQuery())) {
                url = url + "&";
            } else {
                url = url + "?";
            }
            return ToolsUtil.getHtml(url + EPayNotify.getParameter(notify));
        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return ToolsUtil.errorHtml("商户回调地址错误!");
    }

    public String testNotify(EPayNotify ePayNotify) {
        System.out.printf("%s\n", ePayNotify);
        if (ePayNotify.getPid() == 0) return "error";
        User user = userRepository.findAllByPid(ePayNotify.getPid());
        boolean verify = ePayNotify.isSign(user.getSecretKey());
        System.out.printf(verify ? "效验成功！\n" : "效验失败!\n");
        if (verify) {
            return "success";
        }
        return "error";
    }
    public ModelAndView testReturn(EPayNotify ePayNotify) {
        if (ePayNotify.getPid() == 0) return ToolsUtil.errorHtml("商户不存在!");
        User user = userRepository.findAllByPid(ePayNotify.getPid());
        return ToolsUtil.errorHtml(ePayNotify.isSign(user.getSecretKey()) ? "效验成功！" : "效验失败!");
    }

    public String alipayNotify(AlipayNotifyParam param, HttpServletRequest req) {
        if (StringUtils.isEmpty(param.getApp_id())) return "fail";


        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(param.getApp_id());
        if (partys.isEmpty()) {
            return "error";
        }
        ThirdParty party = partys.get(0);
        if(!AlipayUtil.checkSign(req, party)) return "error";
        Order order = orderRepository.findAllByOutTradeNo(param.getOut_trade_no());
        if (order == null) {
            return "error";
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return "error";
        }
        if (order.getState() == 1) {
            return "success";
        }
        order.setUpdateTime(System.currentTimeMillis());
        order.setState(1);
        order.setNotifyState(0);
        order.setTradeNo(param.getTrade_no());
        order.setTotalFee(new BigDecimal(String.valueOf(param.getTotal_amount())).doubleValue());
        double fee = 0D;
        Fee f = new Fee();
        f.setAddTime(System.currentTimeMillis());
        f.setUid(user.getId());
        f.setOrderId(order.getId());
        f.setTip("第三方通道手续费");
        /**
         * 扣除通道费
         */
        if (party.getRate() > 0) {
            Double rate = party.getRate();
            Double rateFee = 0D;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                } else {
                    rateFee = order.getTotalFee();
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
            }
            fee = ToolsUtil.getMoney(rateFee);
            f.setPartyFee(rateFee);
        }
        /**
         * 扣除自定义费率
         */
        if (user.getRate() > 0) {
            Double rate = user.getRate();
            Double rateFee = 0D;
            Double totalFee = order.getTotalFee() - fee;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                } else {
                    rateFee = order.getTotalFee();
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
                if (rateFee > totalFee) {
                    rateFee = totalFee;
                }
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setRateFee(ToolsUtil.getMoney(rateFee));
        }
        /**
         * 扣除单笔手续费
         */
        if (user.getFee() > 0) {
            Double totalFee = order.getTotalFee() - fee;
            Double rateFee = 0D;
            if (user.getFee() > totalFee) {
                rateFee = totalFee;
            } else {
                rateFee = user.getFee();
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setSelfFee(ToolsUtil.getMoney(rateFee));
        }
        order.setFee(fee);
        orderRepository.save(order);
        if (f.isNotEmpty()) {
            mongoTemplate.save(f);
        }
        handlerThirdPartyNotify(order);
        return "success";
    }

    public ModelAndView alipay(AlipayNotifyParam param, HttpServletRequest req) {
        if (StringUtils.isEmpty(param.getApp_id())) return ToolsUtil.errorHtml("参数错误");
        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(param.getApp_id());
        if(partys.size() == 0) return ToolsUtil.errorHtml("商户不存在！");
        ThirdParty thirdParty = partys.get(0);
        if (!AlipayUtil.checkSign(req, thirdParty)) return ToolsUtil.errorHtml("效验失败");
        Order order = orderRepository.findAllByOutTradeNo(param.getOut_trade_no());
        if (order == null) {
            return ToolsUtil.errorHtml("订单号不存在!");
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户不存在!");
        }
        if (order.getState() == 0) {
            return ToolsUtil.waitHtml();
        }
        String url = order.getReturnUrl();
        EPayNotify notify = getEPayNotify(order, user);
        try {
            URL u = new URL(url);
            if (StringUtils.isNotEmpty(u.getQuery())) {
                url = url + "&";
            } else {
                url = url + "?";
            }
            return ToolsUtil.getHtml(url + EPayNotify.getParameter(notify));
        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return ToolsUtil.errorHtml("商户回调地址错误!");
    }

    public String wxPayNotify(HttpServletRequest request) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream( )));
            String line;
            StringBuilder sb = new StringBuilder( );
            while ((line = br.readLine( )) != null) {
                sb.append(line);
            }
            if (StringUtils.isBlank(sb)) {
                return "error";
            }
            //支付结果通知的xml格式数据
            String notifyData = sb.toString();
            Map< String, String > notifyMap = WXPayUtil.xmlToMap(notifyData);
            if (!"SUCCESS".equals(notifyMap.get("result_code"))) return "error";
            String out_trade_no = notifyMap.get("out_trade_no");
            if (StringUtils.isEmpty(out_trade_no)) return "error";
            String mch_id = notifyMap.get("mch_id");
            if (StringUtils.isEmpty(mch_id)) return "error";
            List<ThirdParty> thirdPartyList = thirdPartyRepository.findAllByMchId(mch_id);
            if (thirdPartyList.size() == 0) return "error";
            ThirdParty party = thirdPartyList.get(0);
            if (!WxPayUtil.notifyUrl(party,notifyMap)) return "error";
            Order order = orderRepository.findAllByOutTradeNo(out_trade_no);
            User user = userRepository.findAllById(order.getUid());
            if (order == null || user == null) return "error";
            if (order.getState() == 1) {
                return "success";
            }
            order.setUpdateTime(System.currentTimeMillis());
            order.setState(1);
            order.setNotifyState(0);
            order.setTradeNo(notifyMap.get("transaction_id"));
            order.setTotalFee(new Long(notifyMap.get("total_fee")) / 100D);
            double fee = 0D;
            Fee f = new Fee();
            f.setAddTime(System.currentTimeMillis());
            f.setUid(user.getId());
            f.setOrderId(order.getId());
            f.setTip("第三方通道手续费");
            /**
             * 扣除通道费
             */
            if (party.getRate() > 0) {
                Double rate = party.getRate();
                Double rateFee = 0D;
                if (rate > 1) {
                    if (rate < 100) {
                        rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                    } else {
                        rateFee = order.getTotalFee();
                    }
                } else {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
                }
                fee = ToolsUtil.getMoney(rateFee);
                f.setPartyFee(rateFee);
            }
            /**
             * 扣除自定义费率
             */
            if (user.getRate() > 0) {
                Double rate = user.getRate();
                Double rateFee = 0D;
                Double totalFee = order.getTotalFee() - fee;
                if (rate > 1) {
                    if (rate < 100) {
                        rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                        if (rateFee > totalFee) {
                            rateFee = totalFee;
                        }
                    } else {
                        rateFee = order.getTotalFee();
                        if (rateFee > totalFee) {
                            rateFee = totalFee;
                        }
                    }
                } else {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                }
                fee = fee + ToolsUtil.getMoney(rateFee);
                f.setRateFee(ToolsUtil.getMoney(rateFee));
            }
            /**
             * 扣除单笔手续费
             */
            if (user.getFee() > 0) {
                Double totalFee = order.getTotalFee() - fee;
                Double rateFee = 0D;
                if (user.getFee() > totalFee) {
                    rateFee = totalFee;
                } else {
                    rateFee = user.getFee();
                }
                fee = fee + ToolsUtil.getMoney(rateFee);
                f.setSelfFee(ToolsUtil.getMoney(rateFee));
            }
            order.setFee(fee);
            orderRepository.save(order);
            if (f.isNotEmpty()) {
                mongoTemplate.save(f);
            }
            handlerThirdPartyNotify(order);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
