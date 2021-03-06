package com.ny.toutiao.controller;

import com.ny.toutiao.aspect.LogAspect;
import com.ny.toutiao.model.User;
import com.ny.toutiao.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.util.*;

/**
 * Created by ny on 2017/7/29.
 */
//@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    //这里通过注解，也可以是通过配置文件
    //通过依赖注入控制反转(IOC)
    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(HttpSession session){
        logger.info("Visit Index");
        return "Hello NowCoder " + session.getAttribute("msg")
        +"<br> Say:"+ toutiaoService.say();

    }

    @RequestMapping(value= {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") String userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "ny") String key){
        return String.format("GID{%s},UID{%s},TYPE{%d},KEY{%s}",groupId,userId,type,key);
    }

//    @RequestMapping(value={"/vm"})
//    public String news(){
//        return "news";
//    }

    @RequestMapping(value={"/vm"})
    public String news(Model model){
        model.addAttribute("value1","vv1");

        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});

        Map<String,String>  map = new HashMap<>();
        for(int i = 0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }

        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("Jim"));
        return "news";
    }

    @RequestMapping(value= {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        for(Cookie cookie: request.getCookies()){
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }

        sb.append("getMethod:" + request.getMethod()+"<br>");
        sb.append("getPathInfo():" + request.getPathInfo()+"<br>");
        sb.append("getQueryString():" + request.getQueryString()+"<br>");
        sb.append("getRequestURI():" + request.getRequestURI()+"<br>");

        return sb.toString();
    }

    @RequestMapping(value= {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "nyid", defaultValue = "a") String nyid,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value",defaultValue = "value") String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "nyID From Cookie:"+nyid;
    }

    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session) {
        RedirectView red = new RedirectView("/", true);
        //301 永久性跳转 302 临时性跳转
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("msg","Jump from redirect");
        return red;
    }
//    @RequestMapping("/redirect/{code}")
//  //这种跳转永远是是302跳转
//    public String redirect(@PathVariable("code") int code){
//
//        return "redirect:/";
//    }
    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required = false) String key){
        if ("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("key error");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }
}
