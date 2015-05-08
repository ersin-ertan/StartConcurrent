package com.nullcognition.startconcurrent.waitnotify;// Created by ersin on 07/05/15

import java.util.ArrayList;
import java.util.List;

public class CoolingSystem extends Thread{

	List<Overheatable> overheatables = new ArrayList<>(3);

	@Override
	public void run(){
		while(true){
			for(Overheatable o : overheatables){
				o.cooling(1);
			}
			try{Thread.sleep(500);} catch(InterruptedException e){e.printStackTrace();}
		}
	}

	public void addOverheatable(Overheatable o){ overheatables.add(o);}

	public void removeOverheatable(Overheatable o){ overheatables.remove(o);}


}
