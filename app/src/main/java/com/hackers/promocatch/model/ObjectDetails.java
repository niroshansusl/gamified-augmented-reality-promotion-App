package com.hackers.promocatch.model;

import com.google.gson.annotations.SerializedName;

public class ObjectDetails {
	
	@SerializedName("id")
	public String id;

	@SerializedName("longitude")
	public String longitude;
	
	@SerializedName("latitude")
	public String latitude;

	@SerializedName("altitude")
	public String altitude;

	@SerializedName("colour")
	public String colour;

	@SerializedName("offer")
	public String offer;

}
