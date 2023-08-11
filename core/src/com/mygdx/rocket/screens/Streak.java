package com.mygdx.rocket.screens;

import com.badlogic.gdx.utils.TimeUtils;

public class Streak {
    public float x;
    public float y;
    public float radius;
    public float opacity;
    public long creationTime;


    public Streak(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.opacity = 1.0f; // Start with full opacity
        this.creationTime = TimeUtils.nanoTime(); // Store the creation time
    }
}