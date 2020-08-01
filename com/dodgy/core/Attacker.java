package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Attacker {

	Vector2 startPos, endPos;
	float dirX, dirY;
	float size, vel, rotation;
	boolean onScreen = false, alive = true;
	boolean chomps;
	float opacity = 0;

	float rot = 0, maxRot = 45;
	boolean rotR = false;

	Sound crash = Gdx.audio.newSound(Gdx.files.internal("audio/crash.wav"));
	
	String enemyType;

	float volume = 1;

	public Attacker() {
		startPos = new Vector2(100, 100);
		endPos = new Vector2(MathPlus.randInt(0, Gdx.graphics.getWidth()),
				MathPlus.randInt(0, Gdx.graphics.getHeight()));
		boolean left = MathPlus.randBool(), top = MathPlus.randBool();

		if (left) {
			startPos.x = MathPlus.randInt(-Gdx.graphics.getWidth(), 0);
		} else {
			startPos.x = MathPlus.randInt(Gdx.graphics.getWidth(),
					Gdx.graphics.getWidth() * 2);
		}

		if (top) {
			startPos.y = MathPlus.randInt(Gdx.graphics.getHeight(),
					Gdx.graphics.getHeight() * 2);
		} else {
			startPos.y = MathPlus.randInt(-Gdx.graphics.getHeight(), 0);
		}

		dirX = endPos.x - startPos.x;
		dirY = endPos.y - startPos.y;
		double norm = Math.sqrt(dirX * dirX + dirY * dirY);
		dirX /= norm;
		dirY /= norm;

		rotation = Gdx.graphics.getHeight() / 60;
		
		if (MathPlus.randBool()) rotation = -rotation;
	}

	// for blooks
	public Attacker(boolean sidex, boolean sidey, float size) {
		vel = Gdx.graphics.getWidth() / 1.5f;

		startPos = new Vector2(100, 100);
		endPos = new Vector2(MathPlus.randInt(0, Gdx.graphics.getWidth()),
				MathPlus.randInt(0, Gdx.graphics.getHeight()));

		if (sidex) startPos.x = MathPlus.randInt(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * 2);
		else startPos.x = MathPlus.randInt(-Gdx.graphics.getWidth(), 0);

		if (sidey) startPos.y = Gdx.graphics.getHeight() - (size * 2);
		else startPos.y = size * 2;

		dirX = endPos.x - startPos.x;
		dirY = endPos.y - startPos.y;
		double norm = Math.sqrt(dirX * dirX + dirY * dirY);
		dirX /= norm;
		dirY /= norm;

		rotation = 0;
	}

    public Attacker(boolean isTopper) {
        vel = Gdx.graphics.getWidth() / 1.5f;

        startPos = new Vector2(100, 100);
        endPos = new Vector2(MathPlus.randInt(0, Gdx.graphics.getWidth()),
                MathPlus.randInt(0, Gdx.graphics.getHeight()));

        startPos.set(100, 100);

        dirX = endPos.x - startPos.x;
        dirY = endPos.y - startPos.y;
        double norm = Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= norm;
        dirY /= norm;

        rotation = 0;
    }

	public void update() {
		if (alive) {
			if(Dodgy.audio_on)
				volume = 1;
			else
				volume = 0;

			if (bounds().overlaps(Dodgy.screen)) {
				onScreen = true;
				if (opacity < .8)
					opacity += .03f;
			}
			if (onScreen && !bounds().overlaps(Dodgy.screen)) {
				//dispose();
			    alive = false;
			}

			if (bounds().overlaps(Dodgy.player.getBounds()) && alive
					&& !Player.inv) {
				Dodgy.paused = true;
				Dodgy.deathFlash = true;
				crash.play(volume);
				Dodgy.player.boom();
			}

			if (onScreen) {
				for (Attacker a : GameScreen.enemies) {
					if (a.alive && a.enemyType == "chomps" && this.enemyType == "topper") {
						if (bounds().overlaps(a.bounds())) {
							this.alive = false;
							a.alive = false;
							crash.play(volume);
						}
					}
				}
			}
		}
	}

	public void render() {
	}

	public void dispose() {
		alive = false;
		crash.dispose();

	}

	public Rectangle bounds() {
		return new Rectangle(startPos.x + (size / 6), startPos.y + (size / 6),
				size / 1.5f, size / 1.5f);
	}

	public Rectangle killBounds() {
		float s = size * 1.8f;
		return new Rectangle(startPos.x - (s / 2) + (size / 2), startPos.y
				- (s / 2) + size / 2, s, s);
	}

}
