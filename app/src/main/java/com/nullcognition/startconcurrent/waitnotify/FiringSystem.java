package com.nullcognition.startconcurrent.waitnotify;
// Created by ersin on 07/05/15

public class FiringSystem{

	CoolingSystem coolingSystem = new CoolingSystem();
	IonCannon ionCannon = new IonCannon();

	Pelter[] pelters = new Pelter[2];
	Thread pelterController;
	Pelter pelterControllersPelter;

	public FiringSystem(){
		pelters[0] = new V1();
		pelters[1] = new V3();
		pelterControllersPelter = pelters[0];
		pelterController = new Thread(pelterControllersPelter);
		// *** Pelter controller which extends thread was not taking the pelters runnables
		// had to use a plain thread to input the runnable to the constructor

		coolingSystem.addOverheatable(ionCannon);
		coolingSystem.addOverheatable(pelterControllersPelter);
	}

	public void startUnits(){
		ionCannon.start();
		pelterController.start();
		coolingSystem.start();
	}

	public void switchPelter(){
		coolingSystem.removeOverheatable(pelterControllersPelter);
		pelterControllersPelter.setRunning(false); // required to stop pelter, and cleanly
		// switch to the next pelter. Below null added for chance of a memory leak
		pelterController = null;
		startPelter();
	}

	private void startPelter(){
		if(pelterControllersPelter == pelters[0]){ pelterControllersPelter = pelters[1]; }
		else{ pelterControllersPelter = pelters[0]; }

		coolingSystem.addOverheatable(pelterControllersPelter);

		pelterController = new Thread(pelterControllersPelter);
		pelterController.start();
	}


}

