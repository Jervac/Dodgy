package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    float x, y;
    float sizex, sizey;
    Rectangle rect = new Rectangle(-10, -10, 0, 0);
    boolean alive = true;
    float vel;

    public Bullet(float x, float y, float vel) {
        this.x = x;
        this.y = y;
        sizey = Gdx.graphics.getWidth() * 0.02f;
        sizex = sizey;
        this.vel = vel * 1.5f;
    }

    public void update() {
        if(alive) {
            y -= vel * Gdx.graphics.getDeltaTime();
            rect.set(x, y, sizex, sizey);

            if (rect.overlaps(Dodgy.player.getBounds()) && alive && !Player.inv) {
                Dodgy.paused = true;
                Dodgy.deathFlash = true;
                crash.play();
                Dodgy.player.boom();
            }
        }
    }

    public void render() {
        if(alive) {
            Dodgy.sr.setColor(Dodgy.themeColorDark);
            Dodgy.sr.begin(ShapeRenderer.ShapeType.Filled);
            Dodgy.sr.rect(x, y, sizex, sizey);
            Dodgy.sr.end();
        }
    }

    public void dispose() {
        alive = false;
    }
}