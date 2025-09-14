package org.woen.Architecture.EventBus;

import com.qualcomm.robotcore.util.RobotLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EventBus {
    private static final EventBus Instance = new EventBus();
    private static final EventBus ListenersRegistration = new EventBus();

    public static EventBus getInstance() {
        return Instance;
    }
    public static EventBus getListenersRegistration() {return ListenersRegistration;}

    HashMap< Class<? extends IEvent>, ArrayList<OnEventMethod>> eventUsers = new HashMap<>();

    public <K,T extends IEvent<K>> void  subscribe(Class<T> eventType, OnEventMethod<K,T> onEventMethod){
        if(!eventUsers.containsKey(eventType)){
            eventUsers.put(eventType, new ArrayList<>( Arrays.asList( onEventMethod ) ));
        }else{
            eventUsers.get(eventType).add(onEventMethod);
        }
    }

    public <K,T extends IEvent<K>> void invoke(T event){
        ArrayList<OnEventMethod> subscribers = eventUsers.get(event.getClass());
        RobotLog.dd("event tray",event.getClass().getSimpleName());
        for (OnEventMethod i: subscribers) {
            i.onEvent(event);
        }
    }
}
