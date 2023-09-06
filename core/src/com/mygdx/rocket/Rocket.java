package com.mygdx.rocket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mygdx.rocket.build.AttachmentPoint;
import com.mygdx.rocket.build.Part;

public class Rocket {

    private String name;
    private Array<Part> parts;

    public Rocket(String name) {
        this.name = name;
        parts = new Array<Part>();
    }

    public void drawPoints(ShapeRenderer shapeRenderer) {
        for(Part part : parts) {
            part.drawPoints(shapeRenderer);
        }
    }


    public void draw(ShapeRenderer shapeRenderer, Batch batch, Stage stage) {
        for (Part selectedPart : parts) {
            if (selectedPart.getType().equals("fuelTank")) {
                // set up for drawing filled shapes
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                // draw a white rectangle
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(selectedPart.getLocation().x, selectedPart.getLocation().y,
                        selectedPart.getSize().x * 40, selectedPart.getSize().y * 40);

                shapeRenderer.end(); // end of drawing filled shapes

                // set up for drawing lines (i.e., the border)
                Gdx.gl.glLineWidth(5f);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                // draw a gray border around the rectangle
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(selectedPart.getLocation().x, selectedPart.getLocation().y,
                        selectedPart.getSize().x * 40, selectedPart.getSize().y * 40);

                shapeRenderer.end();

            } else if (selectedPart.getType().equals("engine")) {
                float x = selectedPart.getLocation().x;
                float y = selectedPart.getLocation().y;
                float width = selectedPart.getSize().x * 40;
                float height = selectedPart.getSize().y * 40;

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.DARK_GRAY);

                switch (selectedPart.getRotation()) {
                    case 3: // Up
                        shapeRenderer.triangle(x, y, x + width, y, x + width / 2, y + height);
                        break;
                    case 2: // Right
                        shapeRenderer.triangle(x + height, y, x + height, y + width, x, y + width / 2);
                        break;
                    case 1: // Down
                        shapeRenderer.triangle(x, y + height, x + width, y + height, x + width / 2, y);
                        break;
                    case 4: // Left
                        shapeRenderer.triangle(x, y + width, x, y, x + height, y + width / 2);
                        break;
                }

                shapeRenderer.end();

                // Outline
                Gdx.gl.glLineWidth(5f);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GRAY);

                switch (selectedPart.getRotation()) {
                    case 3: // Up
                        shapeRenderer.line(x, y, x + width, y);
                        shapeRenderer.line(x + width, y, x + width / 2, y + height);
                        shapeRenderer.line(x + width / 2, y + height, x, y);
                        break;
                    case 2: // Right
                        shapeRenderer.line(x + height, y, x + height, y + width);
                        shapeRenderer.line(x + height, y + width, x, y + width / 2);
                        shapeRenderer.line(x, y + width / 2, x + height, y);
                        break;
                    case 1: // Down
                        shapeRenderer.line(x, y + height, x + width, y + height);
                        shapeRenderer.line(x + width, y + height, x + width / 2, y);
                        shapeRenderer.line(x + width / 2, y, x, y + height);
                        break;
                    case 4: // Left
                        shapeRenderer.line(x, y + width, x, y);
                        shapeRenderer.line(x, y, x + height, y + width / 2);
                        shapeRenderer.line(x + height, y + width / 2, x, y + width);
                        break;
                }

                shapeRenderer.end();
            } else if(selectedPart.getType().equals("capsule")) {
                float x = selectedPart.getLocation().x;
                float y = selectedPart.getLocation().y;
                float width = selectedPart.getSize().x * 40;
                float height = selectedPart.getSize().y * 40;

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.DARK_GRAY);

                switch (selectedPart.getRotation()) {
                    case 1: // Up
                        shapeRenderer.triangle(x, y + height/2, x + width, y + height/2, x + width / 2, y + height);
                        shapeRenderer.rect(x, y, width, height/2);
                        break;
                    case 4: // Right
                        shapeRenderer.triangle(x + height/2, y, x + height/2, y + width, x, y + width / 2);
                        shapeRenderer.rect(x + height/2, y, height/2, width);
                        break;
                    case 3: // Down
                        shapeRenderer.triangle(x, y + height/2, x + width, y + height/2, x + width / 2, y);
                        shapeRenderer.rect(x, y, width, height/2);
                        break;
                    case 2: // Left
                        shapeRenderer.triangle(x + height/2, y + width, x + height/2, y, x + height, y + width / 2);
                        shapeRenderer.rect(x, y, height/2, width);
                        break;
                }

                shapeRenderer.end();

                // Outline
                Gdx.gl.glLineWidth(5f);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GRAY);

                switch (selectedPart.getRotation()) {
                    case 1: // Up
                        // Draw Triangle Outline
                        shapeRenderer.line(x, y + height/2, x + width, y + height/2);
                        shapeRenderer.line(x + width, y + height/2, x + width / 2, y + height);
                        shapeRenderer.line(x + width / 2, y + height, x, y + height/2);
                        // Draw Rectangle Outline
                        shapeRenderer.rect(x, y, width, height/2);
                        break;
                    case 4: // Right
                        // Draw Triangle Outline
                        shapeRenderer.line(x + height/2, y, x + height/2, y + width);
                        shapeRenderer.line(x + height/2, y + width, x, y + width / 2);
                        shapeRenderer.line(x, y + width / 2, x + height/2, y);
                        // Draw Rectangle Outline
                        shapeRenderer.rect(x + height/2, y, height/2, width);
                        break;
                    case 3: // Down
                        // Draw Triangle Outline
                        shapeRenderer.line(x, y + height/2, x + width, y + height/2);
                        shapeRenderer.line(x + width, y + height/2, x + width / 2, y);
                        shapeRenderer.line(x + width / 2, y, x, y + height/2);
                        // Draw Rectangle Outline
                        shapeRenderer.rect(x, y, width, height/2);
                        break;
                    case 2: // Left
                        // Draw Triangle Outline
                        shapeRenderer.line(x + height/2, y + width, x + height/2, y);
                        shapeRenderer.line(x + height/2, y, x + height, y + width / 2);
                        shapeRenderer.line(x + height, y + width / 2, x + height/2, y + width);
                        // Draw Rectangle Outline
                        shapeRenderer.rect(x, y, height/2, width);
                        break;
                }

                shapeRenderer.end();
            }
        }
    }


    private Vector2 rotatePoint(Vector2 point, Vector2 pivot, float degrees) {
        float rad = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(rad);
        float cos = (float) Math.cos(rad);

        // Translate point to origin
        point.sub(pivot);

        // Rotate
        float xNew = point.x * cos - point.y * sin;
        float yNew = point.x * sin + point.y * cos;

        // Translate back
        Vector2 newPoint = new Vector2(xNew + pivot.x, yNew + pivot.y);

        return newPoint;
    }

    private Vector2 translatePoint(Vector2 point, Vector2 translation) {
        return point.add(translation);
    }


    @Override
    public String toString() {
        String re = "";
        for(Part part : parts) {
            re += part.getLocation();
            re += "\n";
        }
        return re;
    }

    public void addPart(Part part) {
        parts.add(part);
    }

    public Array<Part> getParts() {
        return parts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updatePoints() {

        Array<AttachmentPoint> points = new Array<>();
        for (int i = 0; i < parts.size; i++) {
            for (AttachmentPoint point : parts.get(i).getAttachmentPoints()) {
                points.add(point);
                point.updateLocation(parts.get(i).getLocation(), parts.get(i).getRotation());
            }
        }

        for (AttachmentPoint point : points) {
            point.setOccupied(false);
        }

        //points.get(0).updateLocation(new Vector2(-100, -100));

//        if(points.size > 4)
//            points.get(4).updateLocation(parts.get(1).getLocation());

        // Use a small threshold for comparing float values
        final float EPSILON = 0.05f;

        for (int i = 0; i < points.size; i++) {
            for (int j = i + 1; j < points.size; j++) {
                if (i != j &&
                        Math.abs(points.get(i).getLocation().x - points.get(j).getLocation().x) < EPSILON &&
                        Math.abs(points.get(i).getLocation().y - points.get(j).getLocation().y) < EPSILON) {
//                    System.out.println("Overlapping Points: " + points.get(i).getLocation() + " and " + points.get(j).getLocation());
                    points.get(i).setOccupied(true);
                    points.get(j).setOccupied(true);
                }
            }
        }
    }


    public Array<AttachmentPoint> getAttachmentPoints() {
        Array<AttachmentPoint> re = new Array<AttachmentPoint>();
        for(Part part : parts) {
            for(AttachmentPoint point : part.getAttachmentPoints()) {
                re.add(point);
            }
        }
        return re;
    }

}
