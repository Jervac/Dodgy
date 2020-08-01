package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;

public class GameScreen implements Screen {

	float timer_duration;
	boolean timer_chomps = false, timer_follower = false, timer_dif = false, timer_stuff; // Time for this enemy to spawn
	int pre_chomps, counter_chomps = 2, pre_follower, counter_follower, pre_dif, counter_dif = 20, pre_stuff, counter_stuff = 20; // Waiting duration after each spawn
	int chompSpawnAmount = 1;
	
	public static ArrayList<com.dodgy.core.Attacker> enemies = new ArrayList<com.dodgy.core.Attacker>();

	public void show() {}

	public void playAgain() {
			counter_follower = 5;
	}

	public void update() {
		 lvlUpdate();

		timer_duration += Gdx.graphics.getDeltaTime();
		Dodgy.timeScore = (int) Math.floor(timer_duration);

		if (!timer_dif && !Dodgy.paused && !Dodgy.paused2) {
			pre_dif = Dodgy.timeScore + counter_dif;
			timer_dif = true;
		}

		if (!timer_chomps && !Dodgy.paused && !Dodgy.paused2) {
			pre_chomps = Dodgy.timeScore + counter_chomps;
			timer_chomps = true;
		}

		if (!timer_follower && !Dodgy.paused && !Dodgy.paused2) {
			pre_follower = Dodgy.timeScore + counter_follower;
			timer_follower = true;
		}

		if (Dodgy.timeScore >= pre_dif && timer_dif) {
			    timer_dif = false;
            if(chompSpawnAmount < 3)
				chompSpawnAmount++;
		}

		if (Dodgy.timeScore >= pre_chomps && timer_chomps) {
				for (int i = 0; i < chompSpawnAmount; ++i) {
					enemies.add(new com.dodgy.core.Chomps());
				}
            timer_chomps = false;
		}

		if (Dodgy.timeScore >= pre_follower && timer_follower) {
				enemies.add(new SideGuy());
                if(Dodgy.timeScore > 10) enemies.add(new Chaser());

			timer_follower = false;
		}

		// iterate like this so you don't remove while looping
		if (!Dodgy.paused2) {
			for (int i = 0; i < enemies.size(); ++i) {
				if (enemies.get(i).alive)
					enemies.get(i).update();
				else
					enemies.remove(enemies.get(i));
			}
		}

	}

	// Change Level effects based off Game Level
	void lvlUpdate() {
		if (!timer_stuff && !Dodgy.paused && !Dodgy.paused2) {
			pre_stuff = Dodgy.timeScore + counter_stuff;
			timer_stuff = true;
		}

		if (Dodgy.timeScore >= pre_stuff && timer_stuff) {
		    for(int i = 0; i < chompSpawnAmount; i++)
			    enemies.add(new com.dodgy.core.Chomps());
			timer_stuff = false;
		}
	}

	void flipOrbRot() {
		Dodgy.orbr = -Dodgy.orbr;
	}

	void rotatePlayer() {
		Dodgy.player.rotL = !Dodgy.player.rotL;
		Dodgy.player.rot = -Dodgy.player.rot;
		Dodgy.player.rotMax = -Dodgy.player.rotMax;
	}

	public void render(float dt) {
		for (com.dodgy.core.Attacker e : enemies) e.render();
	}

	public void dispose() {
		for (com.dodgy.core.Attacker e : enemies) e.dispose();
		for (int i = 0; i < enemies.size(); ++i) {
			enemies.remove(enemies.get(i));
		}
		enemies.clear();
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void resize(int w, int h) {
	}
}
