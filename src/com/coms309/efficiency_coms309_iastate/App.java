package com.coms309.efficiency_coms309_iastate;

import android.graphics.Bitmap;

/**
 * app
 * 
 * Contains information regarding the app class, which will relay information
 * to/from the database and EfficienCy
 * 
 * @authors James Saylor, Richard White
 * 
 */

public class App {

	private String appName;
	private String category;
	private int productivityRating;
	private Bitmap appIcon;
	private String description;

	// Creation methods
	public App(String newName) {
		appName = newName;
		category = "";
		productivityRating = 1;
		description = "";
		appIcon = null;
	}

	public App(String newName, String newCategory, int newProductivityRating) {
		appName = newName;
		category = newCategory;
		productivityRating = newProductivityRating;
		description = "";
		appIcon = null;
	}

	public App(String newName, String newCategory, int newProductivityRating,
			Bitmap newIcon, String newDescription) {
		appName = newName;
		category = newCategory;
		productivityRating = newProductivityRating;
		description = newDescription;
		appIcon = newIcon;
	}

	public App copyApp(String newName) {
		App copiedApp = new App(newName, this.getCategory(),
				this.getProductivityRating(), this.appIcon, this.description);
		return copiedApp;
	}

	// Getters for the name, category, productivity rating, and icon for the app
	@Override
	public String toString() {
		return appName;
	}

	public String getCategory() {
		return category;
	}

	public int getProductivityRating() {
		return productivityRating;
	}

	public Bitmap getIcon() {
		return appIcon;
	}

	public String getDescription() {
		return description;
	}

	// Setters for the category and productivity rating for the app
	// as well as for the icon
	public int setCategory(String newCategory) {
		this.category = newCategory;
		return 0;
	}

	public int setProductivityRating(int newProductivityRating) {
		this.productivityRating = newProductivityRating;
		return 0;
	}

	public int setIcon(Bitmap newIcon) {
		this.appIcon = newIcon;
		return 0;
	}

	public int setDescription(String newDescription) {
		this.description = newDescription;
		return 0;
	}
	
	public void setName(String newName) {
		this.appName = newName;
	}

	// TODO
	// Add other fields to save.
}
