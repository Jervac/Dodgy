package com.dodgy.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Dodgy extends Game implements GestureDetector.GestureListener, InputProcessor {
	public static GameScreen game = new GameScreen();
	public static ShapeRenderer sr;
	public static SpriteBatch g;
	public static OrthographicCamera cam;
	public static Viewport vp;
	public static Rectangle screen;
	public static Preferences prefs;
	public static BitmapFont font, tiny, timerFont, timerFontMid;
	public static Shaker shaker;
	public static Vector3 mousePos = new Vector3(0, 0, 0);
	public static Rectangle mouseRect = new Rectangle(0, 0, 10, 10);
	Vector3 mouse;
	static Music theme;
	public static boolean debug = false, paused = true, paused2 = false, gameLoaded = false, levelSelect = true;
	public static int timeScore = 0, highscore0 = 0, highscore1 = 0, dopeLvl = 0;
    public static int points = 0;
	public static boolean deathFlash = false;
	public static float dOp = 0f;
	public static float dtimer;
	float zoom = 1.0f;
	public static boolean regShake = false;
	public static int difficultyMode = 1;
    public static boolean audio_on = true;
	public static Player player;
	public static Sprite orb;
	public static float orbr = 2.4f;
	static float orbSizeSpeed;
	Sprite whites;
	float uiH, uiW;
	float spacing;
	float level_textW, level_textH;
	float btn_finalY;
	public static Sprite menu_pause;
	Sprite btn_pause;
	Sprite bar_pauseT;
	public static float txt_quitW, txt_quitH;
	static Color themeColor = ColorUtil.blu0, themeColorDark = ColorUtil.blu0_dark;
	Sprite charBar;
	Sound highscore;
	static boolean gotHighScore = false;
	AdHandler handler;
    Sprite replay;
	Sprite scoreBar, optionsBar;
    float sideSPace, w, h;

	public Dodgy(AdHandler handler) {
		this.handler = handler;
	}

    public Dodgy() {}

	public void create() {
		// Util
		sr = new ShapeRenderer();
		shaker = new Shaker();
		cam = new OrthographicCamera();
		vp = new ScreenViewport(cam);
		screen = new Rectangle(3, 3, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
        g = new SpriteBatch();
        highscore = Gdx.audio.newSound(Gdx.files
				.internal("audio/highscore.wav"));
		if (difficultyMode == 0) theme = Gdx.audio.newMusic(Gdx.files.internal("audio/Power Up.wav"));
		else if (difficultyMode == 1) theme = Gdx.audio.newMusic(Gdx.files.internal("audio/Defense Line.mp3"));
		
        initGui();
		initFonts();

		whites = new Sprite(new Texture("white.png"));
		whites.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		orb = new Sprite(new Texture("orb0.png"));
		orb.setSize(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getWidth() / 1.5f);
		orb.setPosition(Gdx.graphics.getWidth() / 2 - orb.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - orb.getHeight() / 2);
		orb.setOriginCenter();
		orb.setAlpha(0.2f);

		player = new Player(Gdx.graphics.getWidth() / 2
				- (Gdx.graphics.getWidth() * 0.08f) / 2,
				Gdx.graphics.getHeight() / 2
						- (Gdx.graphics.getWidth() * 0.08f) / 2);
		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);
		prefs = Gdx.app.getPreferences("Dodgy.dat");
        points = prefs.getInteger("points");
		highscore0 = prefs.getInteger("highscore0");
		highscore1 = prefs.getInteger("highscore1");

		if (prefs.getInteger("plays") > 0)
			prefs.putInteger("plays", prefs.getInteger("plays") + 1);
		if (prefs.getInteger("plays") == 0) {
			prefs.putInteger("plays", prefs.getInteger("plays") + 1);
		}
		prefs.flush();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		gameLoaded = true;
		levelSelect = false;
		mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mouse);
		paused = true;
		setScreen(game);
	}

	public void update() {
		mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mousePos);
		mouseRect.setPosition(mousePos.x, mousePos.y);
		cam.zoom = zoom;
		if (Gdx.app.getType() == Application.ApplicationType.Android && !deathFlash) {
			if (paused || paused2 && highscore0 > 20) handler.showAds(true);
			else handler.showAds(false);
		}
		if (shaker.time > 0) shaker.tick(Gdx.graphics.getDeltaTime());

		if (paused && !deathFlash) {
			float lerp = 7f;
			menu_pause.setX(0.0f);
			charBar.setX((float) Math.floor(charBar.getX()
					+ (0 - charBar.getX()) * lerp
					* Gdx.graphics.getDeltaTime()));
		} else {
			menu_pause.setX(Gdx.graphics.getWidth());
			charBar.setX(Gdx.graphics.getWidth());
		}

		if (paused2 || paused) {
			float lerp = 7f;
			if (!deathFlash) bar_pauseT.setX((float) Math.floor(bar_pauseT.getX() + (0 - bar_pauseT.getX()) * lerp * Gdx.graphics.getDeltaTime()));
		} else {
			bar_pauseT.setX(Gdx.graphics.getWidth());
		}

		uiH = Gdx.graphics.getHeight() * 0.2f;
		uiW = Gdx.graphics.getWidth() * 0.2f;
		spacing = Gdx.graphics.getWidth() * 0.006f;

		if (!paused && !levelSelect && !paused2) {
			orb.setPosition(Gdx.graphics.getWidth() / 2 - orb.getWidth() / 2, Gdx.graphics.getHeight() / 2 - orb.getHeight() / 2);
			orb.setOriginCenter();
			if (difficultyMode == 0)
				orb.rotate(-orbr);
			if (difficultyMode == 1)
				orb.rotate(orbr);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D) && !debug)
			debug = true;
		if (Gdx.input.isKeyPressed(Input.Keys.S) && debug)
			debug = false;
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();
		if (paused && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			playAgain();
			paused = false;
		}

		if (paused && gameLoaded) {
			dtimer += Gdx.graphics.getDeltaTime();
			dopeLvl = 0;
			theme.pause();
			theme.setPosition(0);
		}

		if (timeScore > prefs.getInteger("highscore0") && difficultyMode == 0) {
			prefs.putInteger("highscore0", timeScore);
			prefs.flush();
			highscore0 = timeScore;
			if (!gotHighScore && highscore0 > 0) {
				if(audio_on)
					highscore.play();
				gotHighScore = true;
			}
		}

		if (timeScore > prefs.getInteger("highscore1") && difficultyMode == 1) {
			prefs.putInteger("highscore1", timeScore);
			prefs.flush();
			highscore1 = timeScore;
			if (!gotHighScore && highscore1 > 0) {
				if(audio_on)
					highscore.play();
				gotHighScore = true;
			}
		}

		if (!paused && !paused2) {
			player.update();
			game.update();
		}
	}

	public void render() {
		update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.setProjectionMatrix(cam.combined);
		g.setProjectionMatrix(cam.combined);
		cam.update();
		mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mouse);
		this.getScreen().render(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(themeColor.r, themeColor.g, themeColor.b, 1);
		bar_pauseT.setColor(themeColor);
		orb.setPosition(cam.position.x - orb.getWidth() / 2, cam.position.y
				- orb.getHeight() / 2);

		if (gameLoaded && !levelSelect) {
			g.begin();
			orb.setColor(themeColorDark);
			orb.setAlpha(0.6f);
			orb.draw(g);
			g.end();
			player.render();

			if (!paused && !paused2) {
				g.begin();
				timerFontMid.setColor(orb.getColor());
				timerFontMid.draw(g, " " + timeScore, (Gdx.graphics.getWidth() / 2)
						- (timerFont.getSpaceWidth() * 3.5f),
						Gdx.graphics.getHeight()/2 + timerFont.getLineHeight()/2);
				g.end();
			}

			if (paused) {
				if (deathFlash) {
					screenShake(14);
					if (dOp > 0) dOp -= .01f;
					g.begin();
					whites.setPosition(-(Gdx.graphics.getWidth() / 2),
							-(Gdx.graphics.getHeight() / 2));
					whites.setSize(Gdx.graphics.getWidth() * 2,
							Gdx.graphics.getHeight() * 2);
					whites.draw(g);
					whites.setColor(1, 1, 1, dOp);
					g.end();

					if (dOp <= 0.6f) {
						cam.position.set(Gdx.graphics.getWidth() / 2,
								Gdx.graphics.getHeight() / 2, 0);
					}

					if ((float) Math.floor(dtimer) > .2f) {
						deathFlash = false;
					}
				} else {
					Gdx.gl.glEnable(GL20.GL_BLEND);
					Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
					g.begin();
					charBar.setColor(themeColor);
					g.end();
				}

			}
			if (debug) {
				sr.setColor(Color.RED);
				sr.begin(ShapeRenderer.ShapeType.Line);
				sr.rect(screen.x, screen.y, screen.width, screen.height);
				sr.end();
			}
		}

		g.begin();
		if (!deathFlash) {
			if (!paused2 && !paused) {
				btn_pause.draw(g);
			}
		}
		g.end();

        w = Gdx.graphics.getWidth()/1.2f;
        h = Gdx.graphics.getHeight()/6;
        sideSPace = Gdx.graphics.getWidth() - w;
        sideSPace /=2;

		if (menu_pause.getX() == 0.0f || paused2) {
			sr.setColor(themeColorDark);
			sr.begin(ShapeRenderer.ShapeType.Filled);
			sr.rect(Gdx.graphics.getWidth()/2 - w/2, h/2, w, h);
			sr.rect(Gdx.graphics.getWidth()/2 - w/2, h * 2, w, h * 3);
			sr.end();
			if(scoreBar.getHeight() == 0) {
				scoreBar.setSize(w, h*3);
				scoreBar.setPosition(Gdx.graphics.getWidth()/2 - w/2, h * 2);
				optionsBar.setSize(scoreBar.getWidth(), h);
				optionsBar.setPosition(Gdx.graphics.getWidth()/2 - w/2, h/2);
			}

			g.begin();
			scoreBar.draw(g);
			optionsBar.draw(g);
			font.setColor(Color.WHITE);
			font.draw(g, "" + Dodgy.timeScore + "",Gdx.graphics.getWidth()/2 - level_textH * 2 + (level_textH * 1.5f),
					Gdx.graphics.getHeight() - level_textH *4);

			if (difficultyMode == 0)
				timerFont.draw(g, "Best: " + Dodgy.highscore0 + "",
						Gdx.graphics.getWidth()/2 - level_textH * 2 + (level_textH/4),
						Gdx.graphics.getHeight() - level_textH *8);
			if (difficultyMode == 1)
				timerFont.draw(g, "Best: " + Dodgy.highscore1 + "",
						Gdx.graphics.getWidth()/2 - level_textH * 2 + (level_textH/4),
						Gdx.graphics.getHeight() - level_textH *8);

			if(!audio_on) timerFont.setColor(Color.RED);
			else timerFont.setColor(Color.WHITE);
			timerFont.draw(g, "Sound", Gdx.graphics.getWidth()/2 - level_textH * 2 + (level_textH/4), h + level_textH/2);
			timerFont.setColor(Color.WHITE);
			g.end();
		}

		/*
        if(mouse.x >= Gdx.graphics.getWidth() - (sideSPace * 1.5f) - level_textW
                && mouse.x <= (Gdx.graphics.getWidth() - (sideSPace * 1.5f) - level_textW) + level_textH * 3
                && mouse.y > optionsBar.getY()
                && mouse.y < optionsBar.getY() + optionsBar.getHeight()) {
            System.out.println("touch");
        }
		*/

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) Player.inv = !Player.inv;
	}

	public static void playAgain() {
        if (difficultyMode == 0) theme = Gdx.audio.newMusic(Gdx.files.internal("audio/Power Up.wav"));
        else if (difficultyMode == 1) theme = Gdx.audio.newMusic(Gdx.files.internal("audio/Defense Line.mp3"));

		theme.play();
		theme.setLooping(true);
        if(audio_on) theme.setVolume(1);
        else theme.setVolume(0);

		orbSizeSpeed = player.getSize() / 6;
		regShake = false;
		dtimer = 0;
		dOp = 1;
		paused = false;
		timeScore = 0;
		player.refresh();
		game.dispose();
		game = new GameScreen();
		game.playAgain();
		dopeLvl = 0;
		gotHighScore = false;
    }

	public static void screenShake(int range) {
		cam.translate(MathPlus.randInt(-range, range), MathPlus.randInt(-range, range));
	}

	public void resize(int width, int height) {
		// for repositioning screen on android
		cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		vp.update(width, height);
		this.getScreen().resize(width, height);
		screen.setSize(Gdx.graphics.getWidth() - 5, Gdx.graphics.getHeight() - 5);
	}

	public void dispose() {
		player.dispose();
		this.getScreen().dispose();
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (!paused && gameLoaded && mouse.x >= btn_pause.getX()
				&& mouse.x <= btn_pause.getX() + btn_pause.getWidth()
				&& mouse.y > btn_pause.getY()
				&& mouse.y < btn_pause.getY() + btn_pause.getHeight()) {
			paused2 = !paused2;
			theme.pause();
		} else if (gameLoaded && mouse.x >= Gdx.graphics.getWidth()/2 - level_textH * 2
                && mouse.x <= (Gdx.graphics.getWidth()/2) + (level_textH * 2)
                && mouse.y > optionsBar.getY()
                && mouse.y < optionsBar.getY() + optionsBar.getHeight()) {

		    if(paused2 || paused) {
                audio_on = !audio_on;
                if (!audio_on) {
                    theme.setVolume(0);
                } else {
                    theme.setVolume(1);
                }
            }
        } else if (paused) {
			if (!deathFlash) {
					playAgain();
			}
		} else if (paused2) {
            paused2 = !paused2;
            theme.play();
        }
		return true;
	}

	void initGui() {
	    replay = new Sprite(new Texture(Gdx.files.internal("ui/refresh.png"), true));
        replay.setSize(Gdx.graphics.getWidth() * 0.07f, Gdx.graphics.getWidth() * 0.07f);
        replay.setPosition(Gdx.graphics.getWidth() - replay.getWidth() * 2, 0);
		replay.getTexture().setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        bar_pauseT = new Sprite(new Texture("ui/Button3.png"));
        bar_pauseT.setSize(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight() / 6);
        bar_pauseT.setPosition(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight() - bar_pauseT.getHeight());
        btn_pause = new Sprite(new Texture("ui/pause.png"));
        btn_pause.setSize(Gdx.graphics.getWidth() * 0.05f,
                Gdx.graphics.getWidth() * 0.05f);
        btn_pause.setPosition(Gdx.graphics.getWidth() - btn_pause.getWidth() * 1.2f, Gdx.graphics.getHeight() - btn_pause.getHeight() * 1.2f);
		btn_pause.setColor(themeColorDark);
		btn_pause.setAlpha(0.8f);
		menu_pause = new Sprite(new Texture("ui/Button3.png"));
        menu_pause.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menu_pause.setPosition(Gdx.graphics.getWidth(), 0);
        menu_pause.setAlpha(0.96f);
        charBar = new Sprite(new Texture("ui/Button3.png"));
        charBar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 4);
        charBar.setPosition(menu_pause.getX(), Gdx.graphics.getHeight() / 2 - charBar.getHeight() / 2);
        charBar.setColor(Color.WHITE);
        charBar.setAlpha(0.8f);
        btn_finalY = level_textH / 2;
        scoreBar = new Sprite(new Texture("ui/menu.png"));
        scoreBar.setSize(0,0); // checked if 0 in ui rendering near "menu"
        optionsBar = new Sprite(new Texture("ui/menu2.png"));
        optionsBar.setSize(0,0);
    }

    void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/octin-college-free.regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (Gdx.graphics.getHeight() / 6);
		font = generator.generateFont(parameter);
		font.setColor(Color.WHITE);
        FreeTypeFontGenerator generatorTime = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/octin-college-free.regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterTime = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterTime.size = (Gdx.graphics.getHeight() /12);
        timerFont = generatorTime.generateFont(parameterTime);
		parameterTime.size = (Gdx.graphics.getHeight() /6);
		timerFontMid = generatorTime.generateFont(parameterTime);
		FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter2.size = (int) (Gdx.graphics.getWidth() * 0.02f);
		tiny = generator.generateFont(parameter2);
		tiny.setColor(Color.BLACK);
		generator.dispose();
		GlyphLayout layout2 = new GlyphLayout(timerFont, "Paused");
		level_textW = layout2.width;
		level_textH = layout2.height;
		txt_quitW = layout2.width;
		txt_quitH = layout2.height;
	}

	@Override
	public boolean longPress(float x, float y) {
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return true;
	}

	@Override
	public void pinchStop() {}

	@Override
	public boolean zoom(float initialDistance, float distance) {return true;}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return true;
	}
	
	@Override
	public boolean panStop(float a, float b, int c, int d) {return true;}
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {return true;}
	@Override
	public boolean keyDown(int keycode) {return false;}
	@Override
	public boolean keyUp(int keycode) {return false;}
	@Override
	public boolean keyTyped(char character) {return false;}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	@Override
	public boolean scrolled(int amount) {return false;}
}