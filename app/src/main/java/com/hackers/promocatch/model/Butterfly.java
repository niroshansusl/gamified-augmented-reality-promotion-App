package com.hackers.promocatch.model;

public class Butterfly {

	int _id;
	int butterflyID;
	String ctachTimeStamp;
	double catchLat;
	double catchLon;
	String reward;
	String colour;
	String gamemode;

	public Butterfly(int butterflyID, String ctachTimeStamp, double catchLat,
			double catchLon, String reward, String colour, String gamemode) {
		super();
		this.butterflyID = butterflyID;
		this.ctachTimeStamp = ctachTimeStamp;
		this.catchLat = catchLat;
		this.catchLon = catchLon;
		this.reward = reward;
		this.colour = colour;
		this.gamemode = gamemode;
	}

	public String getGamemode() {
		return gamemode;
	}

	public void setGamemode(String gamemode) {
		this.gamemode = gamemode;
	}

	public Butterfly() {
		super();
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getButterflyID() {
		return butterflyID;
	}

	public void setButterflyID(int butterflyID) {
		this.butterflyID = butterflyID;
	}

	public String getCtachTimeStamp() {
		return ctachTimeStamp;
	}

	public void setCtachTimeStamp(String ctachTimeStamp) {
		this.ctachTimeStamp = ctachTimeStamp;
	}

	public double getCatchLat() {
		return catchLat;
	}

	public void setCatchLat(double catchLat) {
		this.catchLat = catchLat;
	}

	public double getCatchLon() {
		return catchLon;
	}

	public void setCatchLon(double catchLon) {
		this.catchLon = catchLon;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

}
