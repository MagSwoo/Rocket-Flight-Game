package com.mygdx.rocket.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.rocket.RocketFlight;

public class SettingScreen implements Screen {

    private final RocketFlight app;
    private Stage stage;

    private Skin skin;

    public static BitmapFont font48;
    public static BitmapFont font24;

    public float soundValue;
    public float musicValue;
    private Slider soundSlider;
    private Slider musicSlider;

    public SettingScreen(final RocketFlight app) {
        this.app = app;
        this.stage = new Stage(new StretchViewport(RocketFlight.V_WIDTH, RocketFlight.V_HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);
        //create skin for UI
        skin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));
    }

    @Override
    public void show() {
        //initialize fonts
        initFonts();

        //Add sliders for sound and music
        soundSlider = new Slider(0, 100, 1, false, skin);
        soundSlider.setSize(500, 30);
        soundSlider.setPosition(RocketFlight.V_WIDTH / 2 - soundSlider.getWidth() / 2, RocketFlight.V_HEIGHT - 100);
        soundSlider.setValue(50);

        musicSlider = new Slider(0, 100, 1, false, skin);
        musicSlider.setSize(500, 30);
        musicSlider.setPosition(RocketFlight.V_WIDTH / 2 - musicSlider.getWidth() / 2, RocketFlight.V_HEIGHT - 200);
        musicSlider.setValue(50);

        //Adding Main menu button
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.setSize(500, 100);
        mainMenuButton.setPosition(RocketFlight.V_WIDTH / 2 - mainMenuButton.getWidth() / 2, 100);
        //Main Menu Clicklistener
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.setScreen(new MainMenu(app)); // Switch to the main menu screen
                Gdx.app.log("Main Menu", "Clicked");
            }
        });

        //Add UI elements to the stage
        stage.addActor(soundSlider);
        stage.addActor(musicSlider);
        stage.addActor(mainMenuButton);
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Iceland-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 48;
        params.color = Color.WHITE;
        font48 = generator.generateFont(params);
        params.size = 24;
        font24 = generator.generateFont(params);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //batch begin
        app.batch.begin();
        font48.draw(app.batch, "SOUND", RocketFlight.V_WIDTH / 2 - 400, RocketFlight.V_HEIGHT - 70);
        font48.draw(app.batch, "MUSIC", RocketFlight.V_WIDTH / 2 - 400, RocketFlight.V_HEIGHT - 170);

        float soundValue = soundSlider.getValue();
        float musicValue = musicSlider.getValue();
        font24.draw(app.batch, "Sound Value: " + soundValue + "%", RocketFlight.V_WIDTH / 2 - 100, RocketFlight.V_HEIGHT - 100);
        font24.draw(app.batch, "Music Value: " + musicValue + "%", RocketFlight.V_WIDTH / 2 - 100, RocketFlight.V_HEIGHT - 200);

        app.batch.end();

        //stage elements
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        skin.dispose();
        stage.dispose();
    }
}
