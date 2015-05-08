package com.nullcognition.startconcurrent.practice;// Created by ersin on 08/05/15

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class P01{

	StopRunnableThread a;
	StopRunnableThread b;
	StopRunnableThread runningThread = null;
	StopRunnableThread stoppedThread = null;

	RunMe runMe = new RunMe();

	public void start(){
		startThread();
		sleeps();
		startThread();
		sleeps();
		for(int i = 0; i < 3; i++){
			switchThread(runningThread, stoppedThread);
			sleeps();
		}
	}

	private void switchThread(StopRunnableThread stop, StopRunnableThread start){
		if(a == stop){
			a.stopRunnable();
			stoppedThread = a;
			b.startRunnable();
			runMe.notified();
			runningThread = b;
		}
		else{
			b.stopRunnable();
			stoppedThread = b;
			a.startRunnable();
			runMe.notified();
			runningThread = a;
		}
		Log.e("logErr", "---------- Switching threads: stopping " + stop.getName() + ", starting " + start.getName());
	}

	private void startThread(){
		if(runningThread != null){ // TODO - fix/understand thread local and switching threads and calls
			a.stopRunnable(); // not properly stopping the thread local variable from the thread specific thread,
			// reverting to the main thread, hence the runnable in the thread does not stop and continues counting...
			stoppedThread = a;
			b = new StopRunnableThread(runMe);
			b.start();
			Log.e("logErr", "---------- Starting thread " + b.getName());
			runningThread = b;
		}
		else{
			a = new StopRunnableThread(runMe);
			a.start();
			Log.e("logErr", "---------- Starting thread " + a.getName());
			runningThread = a;
		}

	}

	private void sleeps(){
		for(int i = 0; i < 3; i++){
			try{Thread.sleep(1_000);} catch(InterruptedException e){e.printStackTrace();}
			Log.e("logErr", "amount of mods is " + runMe.mods);
			runMe.notified();
		}
	}


}

class StopRunnableThread extends Thread{
	private RunMe runnable;

	public StopRunnableThread(RunMe r){runnable = r;}

	@Override
	public void run(){
		super.run();
		runnable.run();
	}

	public void stopRunnable(){
		Log.e("logErr", "Stopping Runnable from thread " + Thread.currentThread().getName());
		runnable.isRunning.set(Boolean.FALSE);
	}

	public void startRunnable(){
		Log.e("logErr", "Stopping Runnable from thread " + Thread.currentThread().getName());
		runnable.isRunning.set(Boolean.TRUE);
	}
}

class RunMe implements Runnable{
	public ThreadLocal<Boolean> isRunning = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue(){
			return Boolean.TRUE;
		}
	};
	ThreadLocal<AtomicInteger> threadLocal = new ThreadLocal<AtomicInteger>(){
		@Override
		protected AtomicInteger initialValue(){
			return new AtomicInteger(0);
		}
	};
	volatile int mods = 0;

	@Override
	public void run(){
		while(isRunning.get()){
			int temp = threadLocal.get().getAndIncrement();
			synchronized(this){
				if((temp % 100) == 0){
					try{
						++mods;
						Log.e("logErr", Thread.currentThread().getName() + " mod 100");
						wait();
					} catch(InterruptedException e){e.printStackTrace();} finally{
						Log.e("logErr", Thread.currentThread().getName() + " finnaly");
					}
					Log.e("logErr", Thread.currentThread().getName() + "Notified");
				}
			}
		}
	}

	public synchronized void notified(){
		Log.e("logErr", Thread.currentThread().getName() + " notifying ");

		notifyAll();
	}
}
