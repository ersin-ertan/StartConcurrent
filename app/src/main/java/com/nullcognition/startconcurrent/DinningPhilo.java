package com.nullcognition.startconcurrent;

// Created by ersin on 05/05/15

import android.util.Log;

import java.util.PriorityQueue;
import java.util.Random;

public class DinningPhilo{

	private PriorityQueue<Philo> eatingQueue = new PriorityQueue<>(10);

	Thread forkHandler = new Thread(new ForkHandler(eatingQueue));
	Philo[] philos = new Philo[10];

	public void feast(){
		for(int i = 0; i < 10; i++){
			philos[i] = new Philo(eatingQueue, i);
		}

		initNeighbours();
		forkHandler.start();

		for(int i = 0; i < 10; i++){
			philos[i].start();
		}
	}

	private void initNeighbours(){
		philos[0].setLeftRight(philos[9], philos[1]); // edge cases
		philos[9].setLeftRight(philos[8], philos[0]);

		for(int i = 1; i < 9; i++){
			philos[i].setLeftRight(philos[i - 1], philos[i + 1]);
		}
	}
}

class ForkHandler implements Runnable{

	private PriorityQueue<Philo> eatingQueue;

	public ForkHandler(PriorityQueue<Philo> eq){ eatingQueue = eq;}

	@Override
	public void run(){
		while(true){
			if(!eatingQueue.isEmpty()){
				Philo eater = eatingQueue.peek();
				eater.getForks();
			}
		}
	}
}

class Philo extends Thread implements Comparable{

	private PriorityQueue<Philo> eatingQueue;
	static volatile boolean[] forksAreReceived = new boolean[10];

	private Philo pLeft, pRight;
	private int iAmPhiloNumber;
	private int fLeft, fRight;
	private long start, end;


	public Philo(PriorityQueue<Philo> priorityQueue, int number){

		eatingQueue = priorityQueue;
		iAmPhiloNumber = number;
		fLeft = number - 1;
		fRight = number;
		if(number == 0){ fLeft = 9; }
	}

	public void setLeftRight(Philo left, Philo right){
		pLeft = left;
		pRight = right;
	}

	@Override
	public void run(){
		while(true){
			think();
			eat();
			while(eatingQueue.contains(this)){
				incPriority();
				// Log.e("logErr", "philo " + iAmPhiloNumber + " hunger is " + getPriority());
			}
		}
	}

	private void think(){
		Log.e("logErr", "philo " + iAmPhiloNumber + " is thinking");
		sleepMe();
	}

	private void eat(){
		Log.e("logErr", "philo " + iAmPhiloNumber + " is entered queue");
		setPriority(Thread.MIN_PRIORITY);
		eatingQueue.add(this);
		start = System.nanoTime();
	}

	public synchronized void getForks(){
		if(!(forksAreReceived[fLeft] && forksAreReceived[fRight])){

			forksAreReceived[fLeft] = true;
			forksAreReceived[fRight] = true;
			end = System.nanoTime();
			Log.e("logErr", "philo " + iAmPhiloNumber + " got the forks and ate with priority of " + this.getPriority() + "\nTotal time in eating queue is " + (end - start) / 10000000);
			sleepMe();
			forksAreReceived[fRight] = false;
			forksAreReceived[fLeft] = false;
			eatingQueue.remove(this);
			pLeft.incPriority(); // your neighbours are probably hungrier than others because they had to wait for you
			pRight.incPriority();
		}
		else{
			Log.e("logErr", "philo " + iAmPhiloNumber + " missed forks ");

//			eatingQueue.remove(); // recycle the philo to the end of the queue giving another one a chance, as hunger/priority goes up
//			eatingQueue.add(this); // this philo will get more chances to get the forks
		}
	}

	public void incPriority(){ if(getPriority() < 10){ setPriority(getPriority() + 1); }}

	static Random r = new Random(1);

	private void sleepMe(){
		try{
			Thread.sleep(r.nextInt(1000 - 0 + 1) + 0);
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(final Object another){
		Philo p = (Philo) another;
		return this.getPriority() > p.getPriority() ? 1 : this.getPriority() < p.getPriority() ? -1 : 0;
	}
}
