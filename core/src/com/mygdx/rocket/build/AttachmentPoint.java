package com.mygdx.rocket.build;

import com.badlogic.gdx.math.Vector2;

public class AttachmentPoint {

    private Part part;

    private Vector2 position;
    private Vector2 location;
    private int direction;
    private boolean isOccupied;

    public AttachmentPoint(Vector2 position) {
        this.position = position;
        this.isOccupied = false;
        direction = 1;
    }

    public AttachmentPoint(Vector2 position, int direction) {
        this.position = position;
        this.isOccupied = false;
        this.direction = direction;
    }

    public AttachmentPoint(Vector2 position, int direction, Part part) {
        this.position = position;
        this.isOccupied = false;
        this.direction = direction;
        this.part = part;
    }

    public AttachmentPoint(AttachmentPoint point) {
        this.position = new Vector2(point.position);
        this.isOccupied = false;
        this.direction = 0 + point.direction;
    }

    public void updateLocation(Vector2 loc, int directio) {
        if(directio == 1 || directio == 3) {
            location = new Vector2(loc.x + position.x * 40 - 5, loc.y + position.y * 40 - 5);
        } else {
            location = new Vector2(loc.x + position.y * 40 - 5, loc.y + position.x * 40 - 5);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getLocation() {
        return location;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Part getPart() {
        return part;
    }
}
