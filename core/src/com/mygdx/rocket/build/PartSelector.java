package com.mygdx.rocket.build;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class PartSelector {
    private Stage stage;
    private Skin skin;
    private List<String> partCategories;
    private List<Part> partsList;
    private String selectedCategory;
    private Part selectedPart;
    private int categoryNumber;

    private ShapeRenderer shapeRenderer;

    private Array<FuelTank> fuelTanks;
    private Array<Engine> engines;
    private Array<Part> aerodynamics;
    private Array<Part> other;

    private ScrollPane scrollPane;
    private Table table;

    public PartSelector(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        partCategories = new List<>(skin);
        partCategories.setItems(new String[] {"Fuel Tanks", "Engines", "Aerodynamics", "Other"});

        partsList = new List<>(skin);
        partsList.setItems(new Part[0]); // initially empty

        stage.addActor(partsList);

        selectedCategory = partCategories.getItems().get(0);
        categoryNumber = 0;

        // all parts

        // fuel tanks
        fuelTanks = new Array<>();
        fuelTanks.add(new FuelTank("Basic Fuel Tank", 10, new Vector2(2.0f, 2.0f),
                new Array<>(new AttachmentPoint[]{
                    new AttachmentPoint(new Vector2(1.0f, 2.0f), 1),
                    new AttachmentPoint(new Vector2(2.0f, 1.0f), 2),
                    new AttachmentPoint(new Vector2(1.0f, 0.0f), 3),
                    new AttachmentPoint(new Vector2(0.0f, 1.0f), 4)
                }), 9*2000));

        // engines
        engines = new Array<>();
        engines.add(new Engine("Basic Engine", 3.5f, new Vector2(2.0f, 3.0f),
                new Array<>(new AttachmentPoint[]{
                        new AttachmentPoint(new Vector2(1.0f, 3.0f), 1),
                        new AttachmentPoint(new Vector2(1.0f, 0.0f), 3)
                }), 120, 240));

        //aerodynamics
        aerodynamics = new Array<>();

        //other
        other = new Array<>();
        other.add(new Part("Basic Capsule", 4.0f, new Vector2(2.0f, 2.0f),
                new Array<>(new AttachmentPoint[]{
                        new AttachmentPoint(new Vector2(1.0f, 2.0f), 1),
                        new AttachmentPoint(new Vector2(1.0f, 0.0f), 3)
                }))
        );
        other.get(0).setType("capsule");


        positionLists(300, stage.getHeight() ); // example position

        partsList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedPart = partsList.getSelected();
            }
        });
        updatePartsList(0);
        shapeRenderer = new ShapeRenderer();

        scrollPane = new ScrollPane(partsList, skin);
        table = new Table();
        table.add(scrollPane).width(300).height(400);

        stage.addActor(table);
        table.setPosition(200, 400);
    }

    public void positionLists(float x, float y) {
        partCategories.setPosition(x, y);
        partsList.setPosition(x, y - 150); // 150 is the offset, you can adjust it
    }

    public void draw(Batch batch) {
        if (selectedPart != null) {
            if(selectedPart.getType().equals("fuelTank") ) {
                // set up for drawing filled shapes
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                // draw a white rectangle
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(stage.getWidth() - 400, stage.getHeight() / 2,
                        selectedPart.getSize().x * 40, selectedPart.getSize().y * 40);

                shapeRenderer.end(); // end of drawing filled shapes

                // set up for drawing lines (i.e., the border)
                Gdx.gl.glLineWidth(5f);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                // draw a gray border around the rectangle
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(stage.getWidth() - 400, stage.getHeight() / 2,
                        selectedPart.getSize().x * 40, selectedPart.getSize().y * 40);

                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                // draw attachmentPoints
                selectedPart.drawPoints(shapeRenderer);

                shapeRenderer.end(); // end of drawing lines
            }
        }
    }

    public void updatePartsList(int number) {
        if(number == 0) {
            partsList.setItems(fuelTanks);
        } else if(number == 1) {
            partsList.setItems(engines);
        } else if (number == 2) {
            partsList.setItems(aerodynamics);
        } else {
            partsList.setItems(other);
        }
    }

    public Part getSelectedPart() {
        return selectedPart;
    }

    public void setSelectedPart(Part part) {
        selectedPart = part;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(int category) {
        this.selectedCategory = partCategories.getItems().get(category);
        categoryNumber = category;
        updatePartsList(categoryNumber);

        // update the partsList based on the selected category
        // fetch the parts from a manager class based on the selected category
    }

    public void nextCategory() {
        updatePartsList(categoryNumber);
        if(categoryNumber >= partCategories.getItems().size-1) {
            setSelectedCategory(0);
            categoryNumber = 0;
        } else {
            setSelectedCategory(categoryNumber+1);
        }
        updatePartsList(categoryNumber);
        //System.out.println(categoryNumber);
    }

    public void previousCategory() {
        if(categoryNumber == 0) {
            setSelectedCategory(partCategories.getItems().size-1);
            categoryNumber = partCategories.getItems().size-1;
        } else {
            setSelectedCategory(categoryNumber-1);
        }
    }

    // other methods such as positioning the Lists on the screen
}
