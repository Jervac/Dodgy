package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Player {
	float x, y, size;
	float dirX = 0, dirY = 0;
	float endX, endY;
	float startX, startY;
	double norm;
	float maxVel, vel;
	boolean clicked = false;
	public boolean alive = true;
	Sprite player;
	public float rot, rotMax;
	public boolean rotL = false;
	public static boolean inv = false;
	float angle = 90;
	Sound bounce = Gdx.audio.newSound(Gdx.files.internal("audio/bounce.wav"));
	float volume = 1;
	public Player(float x, float y) {
		this.x = x;
		this.y = y;
		maxVel = Gdx.graphics.getWidth() * 0.92f;
		vel = maxVel;
		size = Gdx.graphics.getWidth() * 0.06f;
		player = new Sprite(new Texture(
				Gdx.files.internal("characters/Player_Minimal.png"), true));
		player.setSize(size, size);
		player.setPosition(getX(), getY());
		player.setOriginCenter();
		player.setRotation(90);
		rot = Gdx.graphics.getHeight() / 60;
		rotMax = rot;
	}
	public void render() {
		Dodgy.g.begin();
		player.draw(Dodgy.g);
		Dodgy.g.end();

		if (Dodgy.debug) {
			Dodgy.sr.begin(ShapeRenderer.ShapeType.Line);
			Dodgy.sr.setColor(Color.RED);
			Dodgy.sr.rect(this.getBounds().x, this.getBounds().y,
					this.getBounds().width, this.getBounds().height);
			Dodgy.sr.end();
		}
	}
	public void boom() {}
	public void update() {
		if(Dodgy.audio_on)
			volume = 1;
		else
			volume = 0;
		if (rot > rotMax && !rotL)
			rot = rotMax;
		if (rot < rotMax && rotL)
			rot = rotMax;
		player.setPosition(getX(), getY());
		player.setOriginCenter();
		if (!Dodgy.paused && alive) {
			setX(getX() + dirX * vel * Gdx.graphics.getDeltaTime());
			setY(getY() + dirY * vel * Gdx.graphics.getDeltaTime());
			if (Gdx.input.isTouched() && !clicked) {

				clicked = true;
				vel = maxVel;
				endX = Dodgy.mousePos.x;
				endY = Dodgy.mousePos.y;
				startX = getX() + getSize() / 2;
				startY = getY() + getSize() / 2;
				dirX = endX - startX;
				dirY = endY - startY;
				norm = Math.sqrt(dirX * dirX + dirY * dirY);
				dirX /= norm;
				dirY /= norm;
				rot += rotMax / 4;
				angle = (float) (MathUtils.radiansToDegrees * Math.atan2((endY) - player.getY(), (endX) - player.getX()));
			}
			player.setRotation(angle + 270);
			if (vel > 0)
				vel -= (maxVel) * Gdx.graphics.getDeltaTime()/1.1f;
			if (vel < 0)
				vel = 0;
			if (!Gdx.input.isTouched() && clicked)
				clicked = false;
			if (this.getBounds().getY() + this.getBounds().getHeight() >= Gdx.graphics.getHeight()) {
				dirY = -Math.abs(dirY);
				rot -= (rotMax / 4);
				if (rot < 0 && !rotL)
					rot = 0;
				if (rot > 0 && rotL)
					rot = 0;
				if(vel > 0 || vel < 0)
					angle = -angle;
				
				bounce.play(volume);
			}
			if (this.getBounds().getY() <= 0) {
				dirY = Math.abs(dirY);
				rot -= (rotMax / 4);
				if (rot < 0 && !rotL)
					rot = 0;
				if (rot > 0 && rotL)
					rot = 0;
				if(vel > 0 || vel < 0)
					angle = -angle;
				bounce.play(volume);
			}
			if (this.getBounds().getX() <= 0) {
				dirX = Math.abs(dirX);
				rot -= (rotMax / 4);
				if (rot < 0 && !rotL)
					rot = 0;
				if (rot > 0 && rotL)
					rot = 0;
				if(vel > 0 || vel < 0)
					angle = -angle + 180;
				bounce.play(volume);
			}
			if (this.getBounds().getX() + this.getBounds().getWidth() >= Gdx.graphics.getWidth()) {
				dirX = -Math.abs(dirX);
				rot -= (rotMax / 4);
				if (rot < 0 && !rotL)
					rot = 0;
				if (rot > 0 && rotL)
					rot = 0;
				if(vel > 0 || vel < 0)
					angle = -angle + 180;
				bounce.play(volume);
			}
		}
	}
	public void refresh() {
		player = new Sprite(new Texture(
				Gdx.files.internal("characters/Player_Minimal.png")));
		player.setColor(ColorUtil.blu0_dark);
		player.setPosition(getX(), getY());
		player.setSize(size, size);
		player.setRotation(0);
		vel = 0;
		rot = rotMax;
		angle = 90;
		this.setX(Gdx.graphics.getWidth() / 2
				- (Gdx.graphics.getWidth() * 0.08f) / 2);
		this.setY(Gdx.graphics.getHeight() / 2
				- (Gdx.graphics.getWidth() * 0.08f) / 2);
	}
	public void dispose() {
		player.getTexture().dispose();
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getSize() {
		return size;
	}
	public Rectangle getBounds() {
		return new Rectangle(x + size / 2.3f, y + size / 4f, size / 4, size / 4);
	}
}