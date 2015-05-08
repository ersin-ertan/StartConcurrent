package com.nullcognition.startconcurrent.waitnotify;// Created by ersin on 07/05/15

public interface Overheatable{

	void heating();

	void fail() throws InterruptedException;

	void cooling(int amount);

}
