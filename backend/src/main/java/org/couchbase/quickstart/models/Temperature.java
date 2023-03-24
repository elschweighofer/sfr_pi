package org.couchbase.quickstart.models;


public class Temperature {
    public String getId() {
        return id;
    }

    private Double value;

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


    public Temperature() {
    }

    public Temperature(String id, Double value) {
        this.id = id;
        this.value = value;
    }

    public Temperature(Temperature temperature) {
        this.id = temperature.getId();
        this.value = temperature.getValue();
    }

}
