package com.ny.toutiao.async.handler;

import com.ny.toutiao.async.EventHandler;
import com.ny.toutiao.async.EventModel;
import com.ny.toutiao.async.EventType;
import com.ny.toutiao.model.Message;
import com.ny.toutiao.service.MessageService;
import com.ny.toutiao.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by ny on 2017/8/13.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登陆IP异常");
        // SYSTEM ACCOUNT
        message.setFromId(3);
        message.setCreatedDate(new Date());
        message.setConversationId(String.format("%d_%d", 3,(int)model.getActorId()) );
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("to"), "登陆异常",
                "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
