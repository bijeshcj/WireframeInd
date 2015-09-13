package com.verizontelematics.indrivemobile.controllers;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by z688522 on 10/23/14.
 */
public class RestResponseHandlerThread implements Runnable {
    // response handler thread instance.
    private static RestResponseHandlerThread instance;
    // store a handler in the thread.
    private Handler handler;
    private static final Object mutex = new Object();

    /**
     * initialize the thread here.
     */
    public static void init() {
        try {
            new Thread(new RestResponseHandlerThread()).start();
            synchronized (mutex) {
                mutex.wait();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        synchronized (mutex) {
            instance = this;
            // setup looper.
            Looper.prepare();
            // get handler.
            handler = new Handler();
            // keep looping for exit signs.
            mutex.notify();
        }
        Looper.loop();
    }

    /**
     * return handler here.
     *
     * @return
     */
    public static Handler getHandler() {
        synchronized (mutex) {
            return instance.handler;
        }
    }

//    /**
//     * Quit the thread.
//     */
//    public static void quit() {
//        getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                Looper.myLooper().quitSafely();
//            }
//        });
//    }
}