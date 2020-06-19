package com.example.wanderfoapp.Shop;

public class ExampleItem {
    private String type;
    private String type_value;
    private String name;

    public ExampleItem(String type, String type_value, String name){
        this.type = type;
        this.type_value = type_value;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getType_value() {
        return type_value;
    }

    public String getName() {
        return name;
    }

    public void changeType(String type){
        this.type=type;
    }

    public void changeType_value(String type_value){
        this.type_value=type_value;
    }
}
