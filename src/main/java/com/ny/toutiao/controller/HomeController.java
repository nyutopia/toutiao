package com.ny.toutiao.controller;

import com.ny.toutiao.model.EntityType;
import com.ny.toutiao.model.HostHolder;
import com.ny.toutiao.model.News;
import com.ny.toutiao.model.ViewObject;
import com.ny.toutiao.service.LikeService;
import com.ny.toutiao.service.NewsService;
import com.ny.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ny on 2017/8/2.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    private List<ViewObject> getNews(int userId,int offset,int limit){
        List<News> newsList = newsService.getLatestNews(userId,offset,limit);
//        model.addAttribute("news",newsList);
//        model.addAttribute("users",userlist); 不可行
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for(News news:newsList){
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }


    @RequestMapping(path={"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model,@RequestParam(value = "pop", defaultValue = "0") int pop){

        model.addAttribute("vos",getNews(0,0,10));
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId){

        model.addAttribute("vos",getNews(userId,0,10));

        return "home";
    }
}

