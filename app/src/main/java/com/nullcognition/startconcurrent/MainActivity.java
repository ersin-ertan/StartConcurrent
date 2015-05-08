package com.nullcognition.startconcurrent;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nullcognition.startconcurrent.waitnotify.FiringSystem;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//	  Log.e(getClass().getSimpleName(), Integer.toString(dnaSearchRegEx())); // all methods indicate 1, but there are two
//	  Log.e(getClass().getSimpleName(), Integer.toString(dnaSearch()));
//	  Log.e(getClass().getSimpleName(), Integer.toString(dnaSearchFor()));
//		dinningPhilo();
		firingSystemDeploy();


		int i = 0;


	}

	public void firingSystemDeploy(){
		FiringSystem firingSystem = new FiringSystem();
		firingSystem.startUnits();
		try{
			Thread.sleep(10_000);
		} catch(InterruptedException e){
			e.printStackTrace();
		} finally{
			firingSystem.switchPelter();
		}
	}

	public void dinningPhilo(){
		DinningPhilo dinningPhilo = new DinningPhilo();
		dinningPhilo.feast();
	}

	public int dnaSearchRegEx(){

		Pattern p = Pattern.compile("(.)*TATTA(.)*");
		Matcher m = p.matcher("TATTATTAGATTA");

		int count = 0;
		while(m.find()){ ++count; }
		return count;
	}

	public int dnaSearch(){

		Scanner s = new Scanner("TATTATTAGATTA");

		int count = 0;
		while(s.hasNext("(.)*TATTA(.)*")){
			++count;
			s.next("(.)*TATTA(.)*");
		}
		return count;
	}

	public int dnaSearchFor(){

		String sub = "TATTA";
		String input = "TATTATTAGATTA";

		int count = 0;

		while(input.contains(sub)){
			++count;
			input = input.replace(sub, "");
		}

		return count;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		int id = item.getItemId();

		if(id == R.id.action_settings){
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
