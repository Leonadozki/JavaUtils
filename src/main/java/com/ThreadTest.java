package com;

public class ThreadTest extends Thread{

    public static void main(String[] args) {
        // 匿名方式创建
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 10; i++) {
                    System.out.println("线程运行第"+ i +"次...");
                }
            }
        };
        thread.start();
    }


}
