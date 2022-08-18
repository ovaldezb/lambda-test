package com.ovaldez.aws.crud.lambda.api.model;

import com.google.gson.Gson;

public class Product {

	private int id;
	private String name;
	private double price;
	
	public Product(int id, String name, double price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public Product(String jsonProduct) {
		Gson gson = new Gson();
		Product tempProduct = gson.fromJson(jsonProduct, Product.class);
		this.id = tempProduct.getId();
		this.name = tempProduct.getName();
		this.price = tempProduct.getPrice();
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
