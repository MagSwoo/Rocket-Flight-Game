package com.mygdx.rocket.build;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class FuelTank extends Part{
    private String type;

    //fuel is measured in liters
    private float fuelCapacity;
    private float currentFuel;

    public FuelTank(String name, float mass, Vector2 size, Array<AttachmentPoint> attachmentPoints, float fuelCapacity) {
        super(name, mass, size, attachmentPoints);
        this.fuelCapacity = fuelCapacity;
        this.currentFuel = fuelCapacity; // initially, the tank is full
        super.setType("fuelTank");
    }

    public FuelTank(FuelTank part) {
        super(part);
        this.fuelCapacity = 0 + part.fuelCapacity;
        this.currentFuel = 0 + part.currentFuel;
        super.setType("fuelTank");
    }

    public float getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(float fuelCapacity) {
        if(fuelCapacity >= 0) {
            this.fuelCapacity = fuelCapacity;
        } else {
            throw new IllegalArgumentException("Fuel capacity cannot be negative");
        }
    }

    public float getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(float currentFuel) {
        if(currentFuel >= 0 && currentFuel <= this.fuelCapacity) {
            this.currentFuel = currentFuel;
        } else {
            throw new IllegalArgumentException("Invalid fuel level");
        }
    }

}
