package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class Chomps extends Attacker {

	Sprite sprite, sprite2, shadow;
	float shadowRot = 30;

	public Chomps() {
		enemyType = "chomps";
        vel = Gdx.graphics.getWidth()/2.2f;

		sprite = new Sprite(new Texture("characters/NimblyMinimal.png"));
		sprite2 = new Sprite(new Texture("characters/NimblyMinimal_blu.png"));
		shadow = new Sprite(new Texture("characters/NimblyMinimal.png"));
		shadow.setColor(ColorUtil.blu0_dark);
		sprite.setColor(ColorUtil.blu0_dark);
		shadow.setAlpha(0.5f);

		size = Gdx.graphics.getWidth() * 0.06f;
		sprite.setSize(size, size);
		sprite2.setSize(size, size);
		shadow.setSize(size, size);	
	}

	public void update() {
		if (alive) {
			super.update();

			startPos.x += dirX * vel * Gdx.graphics.getDeltaTime();
			startPos.y += dirY * vel * Gdx.graphics.getDeltaTime();

			if(!rotR) rot+= (Gdx.graphics.getHeight()/62);
	    	if(rot >= maxRot) rotR = true;
	    	if(rotR) rot-= (Gdx.graphics.getHeight()/62);
	    	if(rot <= -maxRot) rotR = false;
	    	
	    	shadowRot += 10;
	    	sprite.setOriginCenter();
			sprite.setRotation(rot);
			sprite2.setOriginCenter();
			sprite2.setRotation(rot);
			shadow.setOriginCenter();
			shadow.setRotation(shadowRot);
		}
	}

	public void render() {
		super.render();
        if (alive) {
            sprite.setPosition(startPos.x, startPos.y);
			sprite2.setPosition(startPos.x, startPos.y);
			shadow.setPosition(startPos.x, startPos.y);
			
			Dodgy.g.begin();
			shadow.draw(Dodgy.g);
			sprite.draw(Dodgy.g);
			Dodgy.g.end();
		}
	}

	public void dispose() {
		super.dispose();
		sprite.getTexture().dispose();
		sprite2.getTexture().dispose();
		shadow.getTexture().dispose();
	}

}
