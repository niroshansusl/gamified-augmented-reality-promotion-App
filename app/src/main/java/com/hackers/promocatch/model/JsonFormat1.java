package com.hackers.promocatch.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class JsonFormat1 {
	
	@SerializedName("status")
	public String status;
	
	@SerializedName("objectList")
	public List<ObjectDetails> objectList;
	
	@SerializedName("dataPackages")
	public List<DataPackage> dataPackages;
}
