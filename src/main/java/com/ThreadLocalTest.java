package com;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalTest {

    static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>(){
        @Override
        protected Map<String, String> initialValue() {
            return new HashMap<>();
        }
    };

    public static void getCurrentThread(){
        System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
    }

    public static void main(String[] args) {
        threadLocal.get().put("leo", "handsome");
        getCurrentThread();

        new Thread(){
            @Override
            public void run() {
                threadLocal.get().put("leo", "smart");
                getCurrentThread();
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                threadLocal.get().put("leo", "hardworking");
                getCurrentThread();
            }
        }.start();
    }
}
