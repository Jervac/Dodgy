package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class SideGuy extends Attacker {
	float startX, startY, endX, endY;
	boolean following = true;
	int stopTime;
    int s, s2;
	float w, h;
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	public SideGuy() {
		enemyType = "follower";
		stopTime = Dodgy.timeScore + 5;
		if(Dodgy.difficultyMode == 0)
			vel = Gdx.graphics.getWidth()/5f;
		if(Dodgy.difficultyMode == 1)
			vel = Gdx.graphics.getWidth()/4f;
		
		size = Gdx.graphics.getWidth() * 0.09f;
		chomps = true;
        s = Dodgy.timeScore;
        s2 = Dodgy.timeScore + 3;

		w = size * 1.5f;
		h = size/2;
	}
	public SideGuy(int nothing) {
		enemyType = "follower";
		stopTime = Dodgy.timeScore + MathPlus.randInt(2, 4);
		if(Dodgy.difficultyMode == 0) vel = Gdx.graphics.getWidth()/4.5f;
		if(Dodgy.difficultyMode == 1) vel = Gdx.graphics.getWidth()/3.5f;
		size = Gdx.graphics.getWidth() * 0.09f;
		chomps = true;
        s = Dodgy.timeScore;
        s2 = Dodgy.timeScore + 3;
		w = size * 1.5f;
		h = size/2;
	}

	public void update() {
		for(Bullet b: bullets) b.update();

		if (alive) {
			super.update();			
			startPos.x += vel * Gdx.graphics.getDeltaTime();
            startPos.y = Gdx.graphics.getHeight() - h;

			if(Dodgy.timeScore >= stopTime) following = false;

			if(following) {
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

			if(!rotR) rot+= (Gdx.graphics.getHeight()/62);
	    	if(rot >= maxRot) rotR = true;
	    	if(rotR) rot-= (Gdx.graphics.getHeight()/62);
	    	if(rot <= -maxRot) rotR = false;
            if(Dodgy.timeScore >= s2) {
                s2 += 69;//so it doesnt have time to shoot again
                bullets.add(new Bullet(startPos.x + size/2, startPos.y, vel * 2));
            }
		}
	}
	public void render() {
		super.render();
		if (alive) {
            Dodgy.sr.setColor(ColorUtil.blu0_dark);
            Dodgy.sr.begin(ShapeRenderer.ShapeType.Filled);
            Dodgy.sr.rect(bounds().x, bounds().y, bounds().getWidth(), bounds().getHeight());
            Dodgy.sr.end();
		}
        for(Bullet b: bullets)
            b.render();
	}
	
	@Override
	public Rectangle bounds() {
	    return new Rectangle(startPos.x, startPos.y, w, h/2);
    }

	public void dispose() {
		super.dispose();
        for(Bullet b: bullets)
            b.dispose();
	}

}
