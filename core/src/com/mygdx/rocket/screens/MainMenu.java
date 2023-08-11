package com.mygdx.rocket.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.rocket.RocketFlight;

import java.util.Iterator;

public class MainMenu implements Screen {

    private final RocketFlight app;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;

    private Image splashImg;
    private Array<Circle> stars;
    private Array<Streak> streaks;
    private long lastDropTime;
    private long lastStreakTime;
    private ShapeRenderer shapeRenderer;

    public static BitmapFont font96;

    public MainMenu(final RocketFlight app) {
        this.app = app;
        this.stage = new Stage(new StretchViewport(RocketFlight.V_WIDTH, RocketFlight.V_HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);
        //create skin for TextButtons
        skin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));
    }

    @Override
    public void show() {
        stars = new Array<Circle>();
        streaks = new Array<Streak>();
        spawnStar();
        shapeRenderer = new ShapeRenderer();

        //initialize fonts
        initFonts();

        //1st button
        TextButton button1 = new TextButton("Begin Simulation", skin);
        button1.setSize(500, 100);
        float buttonWidth = button1.getWidth();
        button1.setPosition(RocketFlight.V_WIDTH/2 - buttonWidth/2, RocketFlight.V_HEIGHT-300);
        //1st button ClickListener
        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle button click here
                Gdx.app.log("Begin Simulation", "Clicked");
            }
        });

        //2nd button
        TextButton button2 = new TextButton("Build Rockets", skin);
        button2.setSize(500, 100);
        buttonWidth = button2.getWidth();
        button2.setPosition(RocketFlight.V_WIDTH/2 - buttonWidth/2, RocketFlight.V_HEIGHT-450);
        //2nd button CLickListener
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle button click here
                Gdx.app.log("Build Rockets", "Clicked");
            }
        });

        //3rd button
        TextButton button3 = new TextButton("Settings", skin);
        button3.setSize(500, 100);
        buttonWidth = button2.getWidth();
        button3.setPosition(RocketFlight.V_WIDTH/2 - buttonWidth/2, RocketFlight.V_HEIGHT-600);
        //3rd button CLickListener
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle button click here
                app.setScreen(new SettingScreen(app));
                Gdx.app.log("Settings", "Clicked");
            }
        });

        // Adding the TextButtons to stage
        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Iceland-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 96;
        params.color = Color.WHITE;
        font96 = generator.generateFont(params);
    }

    private void spawnStar() {
        Circle star = new Circle();
        if(MathUtils.random(0, 4) < 4) {
            star.x = MathUtils.random(0, 1280) + 2;
            star.y = 720 + 2;
        } else {
            star.x = 1280 + 2;
            star.y = MathUtils.random(0,720) + 2;
        }
        star.radius = 2;
        stars.add(star);
        lastDropTime = TimeUtils.nanoTime();
        lastStreakTime = TimeUtils.nanoTime(); // Update lastStreakTime
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        //shape renderer begin
        shapeRenderer.setProjectionMatrix(app.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //draw the circles (stars)
        for (Circle star : stars) {
            shapeRenderer.setColor(1, 1, 1, 1); // White color (R, G, B, Alpha)
            shapeRenderer.circle(star.x, star.y, star.radius);
        }

        //draw the streaks of the stars
        for (Iterator<Streak> iter = streaks.iterator(); iter.hasNext(); ) {
            Streak streak = iter.next();
            streak.opacity = 1.0f - delta * 2f; // Adjust the fade rate as needed
            streak.radius -= delta * 2.5;
            if (streak.radius < 0) {
                iter.remove(); // Remove the streak when opacity reaches 0
            } else {
                shapeRenderer.setColor(1, 1, 1, 1.0f * streak.opacity); // Set opacity
                shapeRenderer.circle(streak.x, streak.y, streak.radius);
                //System.out.println(streak.opacity);
            }
        }

        shapeRenderer.end();

        //batch begin
        app.batch.begin();

        //centering and printing title on the screen
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font96, "ROCKET FLIGHT SIMULATOR");
        float textWidth = glyphLayout.width;
        float x = (RocketFlight.V_WIDTH - textWidth) / 2;

        font96.draw(app.batch, "ROCKET FLIGHT SIMULATOR", x, RocketFlight.V_HEIGHT - 50);
        app.batch.end();

        //spawn star every second
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnStar();

        //iterate through all the stars and change position
        for (Iterator<Circle> iter = stars.iterator(); iter.hasNext(); ) {
            Circle star = iter.next();
            star.y -= 100 * Gdx.graphics.getDeltaTime();
            star.x -= 200 * Gdx.graphics.getDeltaTime();
            if (star.y + 2 < 0 || star.x + 2 < 0) {
                iter.remove();
            } else { //create streaks if not touching edge
                long currentTime = TimeUtils.nanoTime();
                if (currentTime - lastStreakTime >= 10) {
                    Streak streak = new Streak(star.x, star.y, star.radius);
                    streaks.add(streak);
                    lastStreakTime = currentTime;
                }
            }
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);
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
        app.batch.dispose();
        shapeRenderer.dispose();
        font96.dispose();
        skin.dispose();
    }
}
