package com.mygdx.rocket.build;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.rocket.RocketFlight;

public class Part {
    private String type;
    private String name;
    private float mass;
    private Vector2 size;
    private Vector2 location;
    private int rotation;
    private Array<AttachmentPoint> attachmentPoints;
    //private Rectangle bounds;


    public Part(String name, float mass, Vector2 size, Array<AttachmentPoint> attachmentPoints) {
        this.name = name;
        this.mass = mass;
        this.size = size;
        this.attachmentPoints = attachmentPoints;
        //this.bounds = new Rectangle(0, 0, size.x, size.y);
        this.rotation = 1;
        location = new Vector2(RocketFlight.V_WIDTH-400, RocketFlight.V_HEIGHT/2f);
    }

    public Part(Part part) {
        this.type = part.type;
        this.name = "" + part.name;
        this.mass = 0 + part.mass;
        this.size = new Vector2(part.size);
        this.attachmentPoints = new Array<>();
        for (AttachmentPoint point : part.getAttachmentPoints()) {
            this.attachmentPoints.add(new AttachmentPoint(point));
        }
        //this.bounds = new Rectangle(0, 0, part.size.x, part.size.y);
        this.rotation = 0 + part.rotation;
        this.location = new Vector2(part.location);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
    public void drawPoints(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.CYAN);
        //render all attachment points
        for(AttachmentPoint point : attachmentPoints) {
            point.updateLocation(location, rotation);
            if(!point.isOccupied()) {
                shapeRenderer.circle(point.getLocation().x + 5, point.getLocation().y + 5, 5);
            }
        }
    }

    public void rotate() {
        //rotate 90 degrees clockwise
        if(rotation == 4) {
            rotation = 1;
        } else {
            rotation++;
        }
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = new Vector2(location);
    }

    public void flipHorizontal() {
        //flipping logic

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        if(mass >= 0) {
            this.mass = mass;
        } else {
            throw new IllegalArgumentException("Mass cannot be negative");
        }
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Array<AttachmentPoint> getAttachmentPoints() {
        return attachmentPoints;
    }

    public void setAttachmentPoints(Array<AttachmentPoint> attachmentPoints) {
        this.attachmentPoints = attachmentPoints;
    }
}
