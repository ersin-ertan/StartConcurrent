package com.nullcognition.startconcurrent.waitnotify;// Created by ersin on 07/05/15

import android.util.Log;

public class PelterController extends Thread{

	public PelterController(){}

}

abstract class Pelter implements Overheatable, Runnable{
	protected int heat = 0;
	protected int limit;
	private boolean running = true;

	public void setRunning(boolean b){ running = b;}

	@Override
	public void heating(){
		++heat;
		Log.e("logErr", getClass().getSimpleName() + " fired. Heat is:" + heat);
	}

	@Override
	public synchronized void fail() throws InterruptedException{
		Log.e("logErr", "Pelter " + this.getClass().getSimpleName() + " overheated");
		while(heat > 0){ wait();}
		Log.e("logErr", "Pelter " + this.getClass().getSimpleName() + " is ready");
	}

	@Override
	public void cooling(int amount){
		if((heat -= amount) <= 0){
			heat = 0;
			notifyReady();
		}
	}

	private synchronized void notifyReady(){
		notifyAll();
	}

	@Override
	public void run(){ while(running){ fire();}}

	void fire(){
		if(heat < limit){ heating();}
		else{
			try{fail();} catch(InterruptedException e){e.printStackTrace();}
		}
	}
}

class V1 extends Pelter{

	public V1(){limit = 14;}

	@Override
	public void heating(){
		heat += 2;
		Log.e("logErr", "V1 fired. Heat is:" + heat);
		try{
			Thread.sleep(200);
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}

class V3 extends Pelter{

	public V3(){limit = 18;}

	@Override
	public void heating(){
		heat++;
		Log.e("logErr", "V3 fired. Heat is:" + heat);
		try{
			Thread.sleep(100);
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}

