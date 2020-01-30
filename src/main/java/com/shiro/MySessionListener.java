package com.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

public class MySessionListener extends SessionListenerAdapter {

    @Override
    public void onStart(Session session) {
        System.out.println("session create ...");
    }

    @Override
    public void onStop(Session session) {
        System.out.println("session stop");
    }

    @Override
    public void onExpiration(Session session) {
        System.out.println("session expiration");
    }
}
