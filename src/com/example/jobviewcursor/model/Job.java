package com.example.jobviewcursor.model;

public class Job {
	private String title;
	private String date;
	private String description;
	
	public Job(String title,String date,String description){
		this.title = title;
		this.date = date;
		this.description = description;
	}
	
	public String getTitle() {
		return title;
	}
	public String getDate() {
		return date;
	}
	public String getDescription() {
		return description;
	}
}
