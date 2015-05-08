package com.nullcognition.startconcurrent.waitnotify;// Created by ersin on 07/05/15

import android.util.Log;

public class IonCannon extends Thread implements Overheatable{

	int heat = 0;
	int limit = 1;

	@Override
	public void run(){
		while(true){fire();}

	}

	void fire(){
		if(heat < limit){heating();}
		else{
			try{fail();} catch(InterruptedException e){e.printStackTrace();}
		}
	}

	@Override
	public void heating(){
		heat += 10;
		Log.e("logErr", "IonCannon fired. Heat is:" + heat);
	}

	@Override
	public synchronized void fail() throws InterruptedException{
		Log.e("logErr", "IonCannon overheated");
		while(heat > 0){ wait();}
		Log.e("logErr", "IonCannon is ready");
	}

	@Override
	public void cooling(int amount){
		if((heat -= amount) <= 0){
			heat = 0;
			synchronized(this){
				notifyAll();
			}
		}
	}

}
