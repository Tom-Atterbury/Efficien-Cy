package com.coms309.efficiency_coms309_iastate;

import java.util.ArrayList;
import java.util.Comparator;
import android.graphics.Bitmap;

/**
 * AppSummary
 * 
 * Contains additional information over the App class relevant to listing out the apps
 * 
 * @author James Saylor
 * 
 */

public class AppSummary extends App {
	private long totalTime;
	private int percentTime;
	private ArrayList<Coordinates> locationsList;

	public AppSummary(String newName, String newCategory,
			int newProductivityRating, Bitmap newIcon, String newDescription) {
		super(newName, newCategory, newProductivityRating, newIcon, newDescription);
		locationsList = new ArrayList<Coordinates>();
		totalTime = 0;
		percentTime = 0;
	}
	
	public AppSummary(String newName) {
		super(newName);
		locationsList = new ArrayList<Coordinates>();
		totalTime = 0;
		percentTime = 0;
	}
	
	public AppSummary(App newApp) {
		super(newApp.toString(), newApp.getCategory(), newApp.getProductivityRating(), newApp.getIcon(), newApp.getDescription());
		locationsList = new ArrayList<Coordinates>();
		totalTime = 0;
		percentTime = 0;
	}
	
	
	public long getTotalTime() {
		return totalTime;
	}
	
	public int getPercentTime() {
		return percentTime;
	}
	
	public ArrayList<Coordinates> getLocationsList() {
		return locationsList;
	}
	
	public Coordinates getLastLocation() {
		if(locationsList == null)
			return new Coordinates((float) -93.643524, (float) 42.016357);
		else if(locationsList.size() == 0) {
			return MainActivity.getCurrentLocation();
		}
		else if(locationsList.get(locationsList.size() - 1) == null) {
			if(locationsList.get(0) == null) {
				return new Coordinates((float) 1.000, (float) 2.000);
			}
			else {
				return locationsList.get(0);
			}
		}
		else {
			return locationsList.get(locationsList.size() - 1);
		}
		
	}
	
	public void addTotalTime(long addedTime) {
		totalTime += addedTime;
	}
	
	public void setPercentTime(int newPercentTime) {
		percentTime = newPercentTime;
	}
	
	public void setCoordinatesList(ArrayList<Coordinates> newCoords) {
		locationsList = newCoords;
	}
	
	public void addCoordinatesList(ArrayList<Coordinates> newCoords) {
		locationsList.addAll(newCoords);
	}
	
	public void addCoordinates(Coordinates newCoords) {
		locationsList.add(newCoords);
	}

	// TODO: Comparator	
	public static class compareTimes implements Comparator<AppSummary> {
		public int compare(AppSummary o1, AppSummary o2) {
			return (int) (o2.getTotalTime() - o1.getTotalTime());
		}
	}
	
/*	public abstract class Item<AppSummary> implements Comparable<Item<AppSummary>> {
		protected AppSummary item;
		public int compareTo(Item<AppSummary> o) {
			return compareTo(o);
		}
	}*/
}
