package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.httpwrapper.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by z688522 on 10/23/14.
 */
public class OperationsModel extends BaseModel {

    // list of controller operations
    private List<Operation> mOperations = new ArrayList<Operation>();

    private static OperationsModel mOperationModel = null;

    private OperationsModel() {

    }

    public static OperationsModel instance() {
        if (mOperationModel == null) {
            mOperationModel = new OperationsModel();
        }
        return mOperationModel;
    }


    public void setData(Object obj) {
        mOperations.clear();
        mOperations.addAll((List<Operation>)obj);
        setChanged();
        notifyObservers(mOperations);
    }

    public boolean addOperation(Operation aOpr) {
        for (Operation opr : mOperations) {
            if (opr.id == aOpr.id) {
                if (opr.getState() == Operation.ERROR
                        || opr.getState() == Operation.FINISHED
                        || opr.getState() == Operation.CANCELLED) {
                    opr.setState(aOpr.getState());
                    opr.setData(aOpr.getData());
                    // bring operation to top
                    if (mOperations.size() > 1) {
                        mOperations.remove(opr);
                        mOperations.add(0, opr);
                        setChanged();
                        notifyObservers(mOperations);
                    }
                    return true;
                }
                return false;
            }
        }
        mOperations.add(0, aOpr);
        setChanged();
        notifyObservers(mOperations);
        return true;
    }

    public Operation removeOperation(int id) {
        if (mOperations== null || mOperations.isEmpty()) {
            return null;
        }
        Operation temp = null;
        for (Operation opr : mOperations) {
            if (opr.id == id) {
                temp =  opr;
                break;
            }
        }
        if (temp != null) {
            //temp.setView(null);
            temp.deleteObservers();
            mOperations.remove(temp);
            setChanged();
            notifyObservers(mOperations);
        }
        return temp;
    }

    public void removeAllOperations() {
        // check if operation list is already empty.
        if(mOperations == null || mOperations.isEmpty()){
            return;
        }

        // delete Observers for each operation.
        for(Operation opr : mOperations){
            //opr.setView(null);
            opr.clean();
            opr.deleteObservers();
        }

        // clear all operations.
        mOperations.clear();

        // clear all poll operations.
        //PollManager.instance().clean();

        // notify that list is empty to all observers.
        setChanged();
        notifyObservers(mOperations);
    }

    public List<Operation> getOperations() {
        return mOperations;
    }

    public Operation getOperation(int id) {
        if (mOperations== null || mOperations.isEmpty()) {
            return null;
        }

        for (Operation opr : mOperations) {
            if (opr.id == id) {
                return opr;
            }
        }
        return null;
    }

    public Operation getOperation(RestClient.HttpRequest aReq) {

        if (aReq == null)
            return null;

        HashMap<String, String> map = (HashMap<String, String>) aReq.getHeaders();
        String oprStr = map.get("operation");

        if (oprStr == null || oprStr.equals(""))
            return null;

        int oprId = Integer.parseInt(oprStr);
        return (getOperation(oprId));
    }

    @Override
    public Object getData() {
        return mOperations;
    }

    public void clearData() {
        mOperations = null;
    }

}
