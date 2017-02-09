/*
 * Copyright (c) 2015-2016 Luyan Hong
 *
 * This file is part of the Crowdpp.
 *
 * Crowdpp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Crowdpp is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Crowdpp. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.rutgers.winlab.crowdpp.util;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

/**
 * The Now class 
 * A collection of function return the current time of different format
 * @author Luyan Hong
 */
@SuppressLint("SimpleDateFormat")
public class Now {
	/** @return the current time in format of "yyyy-MM-dd'T'HH:mm:ss.SSS" */
	public static String getTime() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return fmt.format(dt); 		
	}
	
	/** @return the current time in format of "yyyy:MM:dd:HH:mm:ss:SSS:z:EEE" */
	public static String getFullTime() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS:z:EEE");
		return fmt.format(dt); 		
	}
	
	/** @return the date of today */
	public static String getDate() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
		return fmt.format(dt); 
	}
	
	/** @return the year of today */
	public static String getYear() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
		return fmt.format(dt); 
	}
	
	/** @return the month of today */
	public static String getMonth() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("MM");
		return fmt.format(dt); 
	}
	
	/** @return the day of today */
	public static String getDay() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("dd");
		return fmt.format(dt); 
	}
	
	
	/** @return the current time in format of "HH:mm" */
	public static String getTimeOfDay() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
		return fmt.format(dt);
	}
	
	/** @return the current time in format of "HH:mm" */
	public static String GetTime() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		return fmt.format(dt);
	}
	
	/** @return the current hour */
	public static String getHour() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("HH");
		return fmt.format(dt);
	}
	
	/** @return the hour before */
	public static String getHourBofore() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("HH");
		int hourbefor = Integer.parseInt(fmt.format(dt))-1;
		String hour;
		if(hourbefor < 10)  hour = "0" + String.valueOf(hourbefor); 
		else hour = String.valueOf(hourbefor);
		//return fmt.format(dt);
		return hour;
	}
	
	/** @return the current minute */
	public static String getMinute() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("mm");
		return fmt.format(dt);
	}
	
	/** @return the current second */
	public static String getSecond() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("ss");
		return fmt.format(dt);
	}

}
