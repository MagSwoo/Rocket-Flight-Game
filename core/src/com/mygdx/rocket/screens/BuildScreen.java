package com.mygdx.rocket.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
//import com.jogamp.opengl.FBObject;
import com.mygdx.rocket.Rocket;
import com.mygdx.rocket.Saves;
import com.mygdx.rocket.build.*;
import com.mygdx.rocket.RocketFlight;

import java.io.File;
import java.io.IOException;

public class BuildScreen implements Screen {

    private final RocketFlight app;
    private Stage stage;

    private Skin skin;
    public BitmapFont font48;
    public BitmapFont font24W;

    private ShapeRenderer shapeRenderer;

    private PartSelector partSelector;
    private Rocket currentRocket;

    private int process = 1;
    private Vector2 location2 = null;
    private int direction2 = 0;


    public BuildScreen(final RocketFlight app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(RocketFlight.V_WIDTH, RocketFlight.V_HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));

        initFonts();
        shapeRenderer = new ShapeRenderer();

        partSelector = new PartSelector(stage, skin);

        currentRocket = new Rocket("UnnamedRocket");
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Iceland-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 48;
        params.color = Color.BLACK;

        // Set the border width and color to simulate a bold effect
        params.borderWidth = 1;
        params.borderColor = Color.BLACK;

        font48 = generator.generateFont(params);

        params.color = Color.WHITE;
        params.size = 24;
        font24W = generator.generateFont(params);

        generator.dispose();
    }

    @Override
    public void show() {
        // Create buttons with the skin
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        TextButton saveButton = new TextButton("Save", skin);
        TextButton loadButton = new TextButton("Load", skin);
        TextButton nextButton = new TextButton("Next", skin);
        TextButton previousButton = new TextButton("Previous", skin);

        // Set positions and sizes for buttons
        mainMenuButton.setSize(200, 50);
        saveButton.setSize(200, 50);
        loadButton.setSize(200, 50);
        nextButton.setSize(100, 50);
        previousButton.setSize(100, 50);

        mainMenuButton.setPosition(RocketFlight.V_WIDTH * 1/3 + 50, 25);
        saveButton.setPosition(RocketFlight.V_WIDTH * 1/3 + 300, 25);
        loadButton.setPosition(RocketFlight.V_WIDTH * 1/3 + 550, 25);
        nextButton.setPosition(200, 40);
        previousButton.setPosition(100, 40);

        // Main Menu Clicklistener
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.setScreen(new MainMenu(app)); // Switch to the main menu screen
                Gdx.app.log("Main Menu", "Clicked");
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                partSelector.nextCategory(); // Switch to the main menu screen
                Gdx.app.log("Next Category", "Clicked");
            }
        });

        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                partSelector.previousCategory(); // Switch to the main menu screen
                Gdx.app.log("Prevous Category", "Clicked");
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                showDialog();
                Gdx.app.log("Save", "Clicked");
            }
        });

        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                showLoadDialog();
                Gdx.app.log("Load", "Clicked");
            }
        });

        // Add UI elements to the stage
        stage.addActor(mainMenuButton);
        stage.addActor(saveButton);
        stage.addActor(loadButton);
        stage.addActor(nextButton);
        stage.addActor(previousButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //shaperenderer begin
        shapeRenderer.setProjectionMatrix(app.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //draw rectangle
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(20, 20, RocketFlight.V_WIDTH / 3 - 40, RocketFlight.V_HEIGHT - 40);

        shapeRenderer.end();

        // batch begin
        app.batch.begin();
        // Drawing title "PARTS"
        font48.draw(app.batch, "PARTS", 140, RocketFlight.V_HEIGHT - 40);
        // drawing Category
        font48.draw(app.batch, partSelector.getSelectedCategory(), 50, 150);
        app.batch.end();

        // Stage elements
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // draw and update rocket
        currentRocket.draw(shapeRenderer, app.batch, stage);

        if(partSelector.getSelectedPart() != null && currentRocket.getParts().isEmpty()) { // if Rocket is empty
            boolean clicked = false;
            app.batch.begin();

            // if rocket has no parts, place first part anywhere
            if(!clicked) {
                font24W.draw(app.batch, "Click anywhere to place the part", RocketFlight.V_WIDTH - 850, RocketFlight.V_HEIGHT - 20);
            }
            // checks if screen is clicked
            if(Gdx.input.isTouched()) {
                float inputX = Gdx.input.getX();
                float inputY = Gdx.input.getY();
                if(inputX >= 500 && inputX <= 1200 && inputY >= 75 && inputY <= 720-75) {
                    clicked = true;
                    partSelector.getSelectedPart().setLocation(new Vector2(inputX - partSelector.getSelectedPart().getSize().x * 20, 720-inputY - partSelector.getSelectedPart().getSize().y * 20));
                    for(AttachmentPoint point : partSelector.getSelectedPart().getAttachmentPoints()) {
                        point.updateLocation(partSelector.getSelectedPart().getLocation(), partSelector.getSelectedPart().getRotation());
                    }
                    partSelector.getSelectedPart().setRotation(1);
                    for(AttachmentPoint point : partSelector.getSelectedPart().getAttachmentPoints()) {
                        point.setPart(partSelector.getSelectedPart());
                    }
                    currentRocket.addPart(partSelector.getSelectedPart());
                    partSelector.setSelectedPart(null);
                }
            }

            app.batch.end();
        } else if (partSelector.getSelectedPart() != null) { // if Rocket already has first part
            Part current = partSelector.getSelectedPart();
            // process value will change depending on which step of the process the user is on

            if(process == 1) {
                // draw attachment points
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                currentRocket.updatePoints();
                currentRocket.drawPoints(shapeRenderer);
                shapeRenderer.end();

                app.batch.begin();
                font24W.draw(app.batch, "Select an attachment point for the piece", RocketFlight.V_WIDTH - 850, RocketFlight.V_HEIGHT - 20);
                app.batch.end();

                if(Gdx.input.isTouched()) {
                    float x = Gdx.input.getX();
                    float y = 720 - Gdx.input.getY();
                        //System.out.println("Initial x: " + x);
                        //System.out.println("Initial y: " + y);
                    for(Part part : currentRocket.getParts()) {
                        for (AttachmentPoint point : part.getAttachmentPoints()) {
                            point.updateLocation(part.getLocation(), part.getRotation());
                            //                            System.out.println("location x: " + point.getLocation().x);
                            //                            System.out.println("location y: " + point.getLocation().y);
                            if (!point.isOccupied() && x >= point.getLocation().x && x <= point.getLocation().x + 10 && y >= point.getLocation().y && y <= point.getLocation().y + 10) {
                                direction2 = point.getDirection();
                                location2 = new Vector2(point.getLocation().x + 5, point.getLocation().y + 5);
                                process++;
                                break;
                            }
                        }
                    }
                }

            } else if (process == 2) {
                // maybe will add rotation here
                process++;

            } else if (process > 2){
                Part currentP = null;
                // create new part and add to rocket
                if(partSelector.getSelectedPart() instanceof FuelTank) {
                    FuelTank current1 = (FuelTank) partSelector.getSelectedPart();
                    currentP = new FuelTank(current1);
                } else if(partSelector.getSelectedPart() instanceof Engine) {
                    Engine current1 = (Engine) partSelector.getSelectedPart();
                    currentP = new Engine(current1);
                } else {
                    currentP = new Part(partSelector.getSelectedPart());
                }
                currentP.setType(partSelector.getSelectedPart().getType());
                if (direction2 == 1 || direction2 == 3) {
                    currentP.setLocation(new Vector2(location2.x - current.getSize().x * 20, location2.y));
                    if(direction2 == 3) {
                        currentP.getLocation().y -= current.getSize().y * 40;
                    }
                } else if (direction2 == 2) {
                    currentP.setLocation(new Vector2(location2.x, location2.y - current.getSize().x * 20));
                } else if (direction2 == 4){
                    currentP.setLocation(new Vector2(location2.x - current.getSize().y * 40,  location2.y - current.getSize().x * 20));
                } else {
                    System.out.println("direction not defined");
                }
                for(AttachmentPoint point : currentP.getAttachmentPoints()) {
                    point.updateLocation(currentP.getLocation(), currentP.getRotation());
                }
                currentP.setRotation(direction2);
                if(currentP.getType().equals("fuelTank")) {
                    currentP.setRotation(1);
                }
                for(AttachmentPoint point : currentP.getAttachmentPoints()) {
                    point.setPart(currentP);
                }
                currentRocket.addPart(currentP);
                partSelector.setSelectedPart(null);
                process = 1;
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font48.dispose();
        stage.dispose();
        shapeRenderer.dispose();
    }

    public void showDialog() {
        // Create the dialog
        final Dialog dialog = new Dialog("Rocket Name", skin);

        // Create a text field for input
        final TextField nameField = new TextField("", skin);
        dialog.text("Enter the name of your rocket:");
        dialog.getContentTable().row();
        dialog.getContentTable().add(nameField);

        // Add "Save" button
        TextButton saveButton = new TextButton("Save", skin);
        dialog.button(saveButton);

        // Add "Cancel" button
        TextButton cancelButton = new TextButton("Cancel", skin);
        dialog.button(cancelButton);

        // Show the dialog
        dialog.show(stage);

        // Add listeners to buttons
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String rocketName = nameField.getText();
                currentRocket.setName(rocketName);
                try {
                    Saves.encode(currentRocket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Gdx.app.log("Save", "Rocket named " + rocketName + " is saved");
                dialog.hide();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
    }

    public void showLoadDialog() {
        // Create the dialog
        final Dialog dialog = new Dialog("Load Rocket", skin);

        // Fetching the list of all text files from the "saves" folder
        FileHandle dirHandle = Gdx.files.internal("saves");
        Array<String> options = new Array<>();
        for (FileHandle entry: dirHandle.list()) {
            if (entry.extension().equals("txt")) {
                options.add(entry.nameWithoutExtension());
            }
        }

        // Create the SelectBox
        final SelectBox<String> selectBox = new SelectBox<>(skin);
        selectBox.setItems(options);

        // Add the SelectBox to the dialog
        dialog.text("Select a rocket to load:");
        dialog.getContentTable().row();
        dialog.getContentTable().add(selectBox);

        // Add "Load" button
        TextButton loadButton = new TextButton("Load", skin);
        dialog.button(loadButton);

        // Add "Cancel" button
        TextButton cancelButton = new TextButton("Cancel", skin);
        dialog.button(cancelButton);

        // Show the dialog
        dialog.show(stage);

        // Add listeners to buttons
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selectedRocket = selectBox.getSelected();
                // Load the selected rocket
                try {
                    File file = new File("saves/" + selectedRocket + ".txt");
                    currentRocket = Saves.decode(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Gdx.app.log("Load", "Rocket " + selectedRocket + " is loaded");
                dialog.hide();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
    }

}


