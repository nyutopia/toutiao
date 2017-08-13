package com.ny.toutiao.async;

import java.util.List;

/**
 * Created by ny on 2017/8/12.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
