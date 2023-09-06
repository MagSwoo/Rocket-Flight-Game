package com.mygdx.rocket;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.rocket.screens.MainMenu;

public class RocketFlight extends Game {
	public static final String TITLE = "Rocket Flight";
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;
	public String screen;
	public OrthographicCamera camera;
	public SpriteBatch batch;

	public BitmapFont font;

	public Array<Rocket> rockets;

	public void addRocket(Rocket rocket) {
		rockets.add(rocket);
	}
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);
		batch = new SpriteBatch();
		font = new BitmapFont();
		screen = "main menu";
		this.setScreen(new MainMenu(this));

	}

	@Override
	public void render () {
		super.render();

		//exit when escape is pressed
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
