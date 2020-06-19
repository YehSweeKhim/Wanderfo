package com.example.wanderfoapp.Home;

public class Category {

  private String shop;
  private int categoryID;

  public Category(String shop, int categoryID){
    this.shop = shop;
    this.categoryID = categoryID;
  }

  public int getcategoryID() {
    return categoryID;
  }

  @Override
  public String toString() {
    return shop;
  }
}