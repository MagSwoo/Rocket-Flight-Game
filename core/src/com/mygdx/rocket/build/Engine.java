package com.mygdx.rocket.build;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Engine extends Part {
    private float thrust; // in newtons
    private float fuelConsumptionRate; // in liters per second

    public Engine(String name, float mass, Vector2 size, Array<AttachmentPoint> attachmentPoints, float thrust, float fuelConsumptionRate) {
        super(name, mass, size, attachmentPoints);
        this.thrust = thrust;
        this.fuelConsumptionRate = fuelConsumptionRate;
        super.setType("engine");
    }

    public Engine(Engine part) {
        super(part);
        this.thrust = 0 + part.getThrust();
        this.fuelConsumptionRate = 0 + part.getFuelConsumptionRate();
        super.setType("engine");
    }

    public float getThrust() {
        return thrust;
    }

    public void setThrust(float thrust) {
        this.thrust = thrust;
    }

    public float getFuelConsumptionRate() {
        return fuelConsumptionRate;
    }

    public void setFuelConsumptionRate(float fuelConsumptionRate) {
        if(fuelConsumptionRate >= 0) {
            this.fuelConsumptionRate = fuelConsumptionRate;
        } else {
            throw new IllegalArgumentException("Fuel consumption rate cannot be negative");
        }
    }
}