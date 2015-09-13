package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.models.MessageControllerOpertation;
import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by z688522 on 3/18/15.
 */
public class MessageControllerOpertationFactory implements OperationFactory {

    private static MessageControllerOpertationFactory mInstance;

    private MessageControllerOpertationFactory(){

    }

    public static MessageControllerOpertationFactory instance(){
        if(mInstance == null){
            mInstance =  new MessageControllerOpertationFactory();
        }
        return mInstance;
    }


    @Override
    public Operation createOperation(int id, int state) {
        return new MessageControllerOpertation(id, state);
    }
}
