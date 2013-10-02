package com.example.fragmenttab;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class BluetoothStatistics extends Activity {
	
	double sexttemp = 0.0, sbodytemp = 0.0, medium = 0.0;
	int slight = 0;
	String s, sdate;
	
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> hour = new ArrayList<String>();
	ArrayList<String> event = new ArrayList<String>();
	ArrayList<String> extTemp = new ArrayList<String>();
	ArrayList<String> bodyTemp = new ArrayList<String>();
	ArrayList<String> light = new ArrayList<String>();
	
	TextView view1;
	TextView view2;
	TextView view3;
	TextView view4;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);
	
/*		
		Intent ricevidati=getIntent();
		date=(ArrayList<String>) ricevidati.getSerializableExtra("date");						
		hour=(ArrayList<String>) ricevidati.getSerializableExtra("time");
		event=(ArrayList<String>) ricevidati.getSerializableExtra("event");
		extTemp=(ArrayList<String>) ricevidati.getSerializableExtra("exttemp");
		bodyTemp=(ArrayList<String>) ricevidati.getSerializableExtra("bodytemp");	
		light=(ArrayList<String>) ricevidati.getSerializableExtra("light");
		
		final int NUMBER_ROW = event.size();

		for(int i=0; i< NUMBER_ROW; i++) {
			
			sexttemp += StringToDouble(extTemp.get(i));
			sbodytemp += StringToDouble(bodyTemp.get(i));
			slight += StringToInt(light.get(i));
		}
		
	//	sdate = date.get(0);
	//	view1.setText(sdate);
		view1 = (TextView) findViewById(R.id.textView1);
		
//		medium = sexttemp/NUMBER_ROW;
	//	s = String.valueOf(medium);
	//	view2.setText(s);
		view2.setText(String.valueOf(sexttemp));
		view2 = (TextView) findViewById(R.id.textView2);
		
//		medium = sbodytemp/NUMBER_ROW;
//		s = String.valueOf(medium);
//		view3.setText(s);
		view3 = (TextView) findViewById(R.id.textView3);
		
//		medium = slight/NUMBER_ROW;
//		s = String.valueOf(medium);
//		view4.setText(s);
		view4 = (TextView) findViewById(R.id.textView4);
		//calcolare la media per ognuno e visualizzarla nella listview con la data */
	
	}/*
	double StringToDouble(String s) {
		
		double result = Double.parseDouble(""
				+ s.charAt(0) + s.charAt(1) + "."
				+ s.charAt(3));

		return result;
		
	}
	
	int StringToInt(String s) {
		
		char semicolon = ';';
		int result = 0;
		if(s.charAt(1) == semicolon) {
			result = Integer.parseInt(""+s.charAt(0));			
		}
		else if(s.charAt(2) == semicolon) {
			result = Integer.parseInt(""+s.charAt(0) + s.charAt(1));			
		}
		else {
			result = Integer.parseInt(""+s.charAt(0) + s.charAt(1) + s.charAt(2));
		}
				
		return result;
	}*/
}
	
