package com.nrk.mobiso;

public class Question {
	public String ownerName;
	public int score;
	public String title;
	
	public Question(String title, int score, String ownerName){
		this.ownerName = ownerName;
		this.score = score;
		this.title = title;
	}
}
