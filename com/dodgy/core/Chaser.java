package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Chaser extends Attacker {

    Sprite sprite;
    float startX, startY, endX, endY;
    float shadowRot = 20;
    boolean following = true;
    int stopTime;

    public Chaser() {
        enemyType = "topper";

        stopTime = Dodgy.timeScore + 3;
        if (Dodgy.difficultyMode == 0)
            vel = Gdx.graphics.getWidth() / 5;
        if (Dodgy.difficultyMode == 1)
            vel = Gdx.graphics.getWidth() / 4;

        sprite = new Sprite(new Texture("characters/Topper.png"));
        sprite.setColor(ColorUtil.blu0_dark);
        size = Gdx.graphics.getWidth() * 0.04f;
        sprite.setSize(size * 2, size);
        chomps = true;
        startPos.set(-size, 100);
        startPos.y = Gdx.graphics.getHeight() - size * 2;
    }

    public void update() {
        if (alive) {
            super.update();
            startPos.x += dirX * vel * Gdx.graphics.getDeltaTime();
            startPos.y += dirY * vel * Gdx.graphics.getDeltaTime();
            if (!Dodgy.paused && !Dodgy.paused2) shadowRot += 20;

            if (Dodgy.timeScore >= stopTime) {
                following = false;
                startPos.y += 20;
            }

            if (alive && startPos.y >= Gdx.graphics.getHeight()) dispose();

            if (following) {
                endX = Dodgy.player.getBounds().x;
                endY = Dodgy.player.getBounds().y;
                startX = startPos.x + size / 2;
                startY = startPos.y + size / 2;
                dirX = endX - startX;
                dirY = endY - startY;
                double norm = Math.sqrt(dirX * dirX + dirY * dirY);
                dirX /= norm;
                dirY /= norm;
            }

            if (!rotR) rot += (Gdx.graphics.getHeight() / 62);
            if (rot >= maxRot) rotR = true;
            if (rotR) rot -= (Gdx.graphics.getHeight() / 62);
            if (rot <= -maxRot) rotR = false;
            sprite.setOriginCenter();
            sprite.setRotation(rot);
        }
    }

    public void render() {
        super.render();
        if (alive) {
            sprite.setAlpha(0.9f);
            sprite.setOriginCenter();
            sprite.setPosition(startPos.x, startPos.y);
            sprite.setColor(ColorUtil.blu0_dark);
            Dodgy.g.begin();
            sprite.draw(Dodgy.g);
            Dodgy.g.end();
        }
    }

    // cant dispose texture cuz its static :p
    public void dispose() {
        super.dispose();
        alive = false;
        sprite.getTexture().dispose();
    }
}