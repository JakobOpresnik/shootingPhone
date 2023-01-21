package com.mygdx.shooting_phone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class ShootingPhone extends ApplicationAdapter {
	SpriteBatch batch;

	private Texture backgroundImage;

	private Texture phoneImage;
	private Texture virusImage;
	private Texture fileImage;
	private Texture imageFileImage;
	private Texture videoFileImage;
	private Texture folderImage;
	private Texture chargeImage;
	private Texture memoryImage;
	private Texture memoryRotatedImage;
	private Texture batteryFullImage;
	private Texture batteryHalfImage;
	private Texture batteryLowImage;
	private Texture batteryVeryLowImage;
	private Texture batteryDeadImage;
	private Texture wifiBulletImage;

	private Sound filePickUpSound;
	private Sound virusCollisionSound;
	private Sound memoryPickUpSound;
	private Sound chargePickUpSound;
	private Music backgroundMusic;

	private OrthographicCamera camera;

	private Rectangle phone;
	private Array<Rectangle> viruses;
	private Array<Rectangle> files;
	private Array<Rectangle> img_files;
	private Array<Rectangle> video_files;
	private Array<Rectangle> memory;
	private Array<Rectangle> bullets;
	private Array<Rectangle> charges;

	private long lastVirusTime;
	private long lastFileTime;
	private long lastImgFileTime;
	private long lastVideoFileTime;
	private long lastMemoryTime;
	private long lastBulletTime;
	private long lastChargeTime;

	private int filesDownloaded;
	private int batteryLife;
	private int memoryCapacity;

	//public BitmapFont font;

	private static final int SPEED = 600;
	private static int SPEED_VIRUS = 200;
	private static final int SPEED_FILE = 100;
	private static final int SPEED_IMG_FILE = 75;
	private static final int SPEED_VIDEO_FILE = 60;
	private static final int SPEED_MEMORY = 80;
	private static final int SPEED_BULLET = 600;
	private static final int SPEED_CHARGE = 600;

	private static final long CREATE_VIRUS_TIME = 700000000;
	private static final long CREATE_FILE_TIME = 2100000000;
	private static final long CREATE_IMG_FILE_TIME = 2120000000;
	private static final long CREATE_VIDEO_FILE_TIME = 2140000000;
	private static final long CREATE_MEMORY_TIME = 2100000000;
	private static final long CREATE_BULLET_TIME = 500000000;
	private static final long CREATE_CHARGE_TIME = 2140000000;

	private BitmapFont font;
	
	@Override
	public void create () {

		//font = new BitmapFont();
		//font.getData().setScale(2);

		// setting up custom font style
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator((Gdx.files.internal("death_star_font.otf")));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 30;
		fontParameter.borderWidth = 2;
		font = fontGenerator.generateFont(fontParameter);

		filesDownloaded = 0;
		batteryLife = 100;	// start with full battery
		memoryCapacity = 0;	// start with 0 memory

		backgroundImage = new Texture(Gdx.files.internal("background.png"));

		// load textures
		phoneImage = new Texture(Gdx.files.internal("phone.png"));
		virusImage = new Texture(Gdx.files.internal("virus.png"));
		fileImage = new Texture(Gdx.files.internal("text_file.png"));
		imageFileImage = new Texture(Gdx.files.internal("image_file.png"));
		videoFileImage = new Texture(Gdx.files.internal("video_file.png"));
		folderImage = new Texture(Gdx.files.internal("folder.png"));
		chargeImage = new Texture(Gdx.files.internal("charge.png"));
		memoryImage = new Texture(Gdx.files.internal("memory.png"));
		batteryFullImage = new Texture(Gdx.files.internal("battery_full.png"));
		batteryHalfImage = new Texture(Gdx.files.internal("battery_half.png"));
		batteryLowImage = new Texture(Gdx.files.internal("battery_low.png"));
		batteryVeryLowImage = new Texture(Gdx.files.internal("battery_really_low.png"));
		batteryDeadImage = new Texture(Gdx.files.internal("battery_dead.png"));
		//memoryImage = new Texture(Gdx.files.internal("memory.png"));
		memoryRotatedImage = new Texture(Gdx.files.internal("memory_rotated.png"));
		wifiBulletImage = new Texture(Gdx.files.internal("wifi.png"));

		// set sound effects
		filePickUpSound = Gdx.audio.newSound(Gdx.files.internal("pop.mp3"));
		virusCollisionSound = Gdx.audio.newSound(Gdx.files.internal("virus_collision.mp3"));
		memoryPickUpSound = Gdx.audio.newSound(Gdx.files.internal("power_up.wav"));
		chargePickUpSound = Gdx.audio.newSound(Gdx.files.internal("heal.wav"));

		// set background music
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.wav"));
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(0.2f);

		backgroundMusic.play();

		// create camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//System.out.print("window size = " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());

		// create sprite batch
		batch = new SpriteBatch();

		// logical representation of game objects
		phone = new Rectangle();
		// centering horizontally
		phone.x = (int) (Gdx.graphics.getWidth() / 2f - phoneImage.getWidth() / 2f);
		phone.y = 20;
		// setting dimensions
		phone.width = phoneImage.getWidth();
		phone.height = phoneImage.getHeight();

		viruses = new Array<Rectangle>();
		files = new Array<Rectangle>();
		img_files = new Array<Rectangle>();
		video_files = new Array<Rectangle>();
		memory = new Array<Rectangle>();
		bullets = new Array<Rectangle>();
		charges = new Array<Rectangle>();

		spawnVirus();
		spawnFile();
		spawnImgFile();
		spawnVideoFile();
		spawnMemory();
		spawnCharge();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		Gdx.gl.glClearColor(0, 0, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		if (TimeUtils.nanoTime() - lastVirusTime > CREATE_VIRUS_TIME) spawnVirus();
		if (TimeUtils.nanoTime() - lastFileTime > CREATE_FILE_TIME) spawnFile();
		if (TimeUtils.nanoTime() - lastImgFileTime > CREATE_IMG_FILE_TIME*2) spawnImgFile();
		if (TimeUtils.nanoTime() - lastVideoFileTime > CREATE_VIDEO_FILE_TIME*3) spawnVideoFile();
		if (TimeUtils.nanoTime() - lastMemoryTime > CREATE_MEMORY_TIME) spawnMemory();
		if (TimeUtils.nanoTime() - lastChargeTime > CREATE_CHARGE_TIME*5) spawnCharge();


		if (batteryLife > 0) {
			// keyboard controls
			if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft();
			if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight();
			if (memoryCapacity > 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) spawnBullet(phone);
			if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) exitGame();

			// viruses
			for (Iterator<Rectangle> it = viruses.iterator(); it.hasNext();) {
				Rectangle virus = it.next();
				// viruses moving downward at virus speed
				virus.y -= SPEED_VIRUS * Gdx.graphics.getDeltaTime();
				// remove viruses below bottom edge of the window
				if (virus.y + virusImage.getHeight() < 0) it.remove();
				// decrement 'health' on collision
				if (virus.overlaps(phone)) {
					virusCollisionSound.play(0.2f);
					batteryLife--;
				}
			}

			// text files
			for (Iterator<Rectangle> it = files.iterator(); it.hasNext();) {
				Rectangle file = it.next();
				// files moving downward at file speed
				file.y -= SPEED_FILE * Gdx.graphics.getDeltaTime();
				// remove files below bottom edge of the window
				if (file.y + fileImage.getHeight() < 0) it.remove();
				// increment score on collision
				if (file.overlaps(phone)) {
					filePickUpSound.play(0.2f);
					filesDownloaded++;
					// speed up the viruses
					if (filesDownloaded % 10 == 0) SPEED_VIRUS += 50;
					it.remove();
				}
			}

			// image files
			for (Iterator<Rectangle> it = img_files.iterator(); it.hasNext();) {
				Rectangle file = it.next();
				// files moving downward at file speed
				file.y -= SPEED_IMG_FILE * Gdx.graphics.getDeltaTime();
				// remove files below bottom edge of the window
				if (file.y + imageFileImage.getHeight() < 0) it.remove();
				// increment score on collision
				if (file.overlaps(phone)) {
					filePickUpSound.play(0.25f);
					// image files give 2 points
					filesDownloaded = filesDownloaded + 2;
					// speed up the viruses
					if (filesDownloaded % 10 == 0) SPEED_VIRUS += 50;
					it.remove();
				}
			}

			// video files
			for (Iterator<Rectangle> it = video_files.iterator(); it.hasNext();) {
				Rectangle file = it.next();
				// files moving downward at file speed
				file.y -= SPEED_VIDEO_FILE * Gdx.graphics.getDeltaTime();
				// remove files below bottom edge of the window
				if (file.y + videoFileImage.getHeight() < 0) it.remove();
				// increment score on collision
				if (file.overlaps(phone)) {
					filePickUpSound.play(0.3f);
					// image files give 3 points
					filesDownloaded = filesDownloaded + 3;
					// speed up the viruses
					if (filesDownloaded % 10 == 0) SPEED_VIRUS += 50;
					it.remove();
				}
			}

			// memory
			for (Iterator<Rectangle> it = memory.iterator(); it.hasNext();) {
				Rectangle memstick = it.next();
				memstick.y -= SPEED_MEMORY * Gdx.graphics.getDeltaTime();
				if (memstick.y + memoryImage.getHeight() < 0) it.remove();
				if (memstick.overlaps(phone)) {
					if (memoryCapacity < 10) {
						memoryPickUpSound.play(0.2f);
						memoryCapacity++;
						it.remove();
					}
				}
			}

			// bullets
			for (Iterator<Rectangle> it = bullets.iterator(); it.hasNext();) {
				Rectangle bullet = it.next();
				bullet.y += SPEED_BULLET * Gdx.graphics.getDeltaTime();
				if (bullet.y + wifiBulletImage.getHeight() > Gdx.graphics.getHeight()) {
					it.remove();
				}
				// collision with viruses
				for (Iterator<Rectangle> itv = viruses.iterator(); itv.hasNext();) {
					if (bullet.overlaps(itv.next())) {
						it.remove();
						itv.remove();
					}
				}
			}

			// charges (heals)
			for (Iterator<Rectangle> it = charges.iterator(); it.hasNext();) {
				Rectangle charge = it.next();
				charge.y -= SPEED_CHARGE * Gdx.graphics.getDeltaTime();
				if (charge.y + chargeImage.getHeight() < 0) it.remove();
				if (charge.overlaps(phone)) {
					if (batteryLife <= 80) {
						chargePickUpSound.play(0.2f);
						batteryLife = batteryLife + 20;
						it.remove();
					}
				}
			}

		}

		// refresh camera (update matrices)
		camera.update();
		// render sprite batch in the coordinate system specified by the camera
		batch.setProjectionMatrix(camera.combined);


		// begin new batch
		// draw phone, viruses & files
		batch.begin();
		{
			// draw background
			batch.draw(backgroundImage, 0, 0);

			// draw phone
			batch.draw(phoneImage, phone.x, phone.y);
			// draw all viruses
			for (Rectangle virus : viruses) {
				batch.draw(virusImage, virus.x, virus.y);
			}
			// draw all text files
			for (Rectangle file : files) {
				batch.draw(fileImage, file.x, file.y);
			}
			// draw all image files
			for (Rectangle img_file : img_files) {
				batch.draw(imageFileImage, img_file.x, img_file.y);
			}
			// draw all video files
			for (Rectangle video_file : video_files) {
				batch.draw(videoFileImage, video_file.x, video_file.y);
			}
			// draw all memory sticks
			for (Rectangle memstick : memory) {
				batch.draw(memoryImage, memstick.x, memstick.y);
			}
			// draw all bullets
			for (Rectangle bullet : bullets) {
				batch.draw(wifiBulletImage, bullet.x, bullet.y);
			}
			// draw all charges
			for (Rectangle charge : charges) {
				batch.draw(chargeImage, charge.x, charge.y);
			}

			// display the score in yellow
			font.setColor(Color.YELLOW);
			font.draw(batch, "" + filesDownloaded, Gdx.graphics.getWidth() - 60, Gdx.graphics.getHeight() - 20);

			// display memory info
			if (memoryCapacity >= 10) {
				font.setColor(Color.RED);
				font.draw(batch, "MEMORY FULL", Gdx.graphics.getHeight() / 2f - 30, Gdx.graphics.getHeight() / 2f);
			}
			else if (memoryCapacity > 0 && memoryCapacity <= 7) font.setColor(Color.ORANGE);
			else if (memoryCapacity > 7) font.setColor(Color.RED);
			else if (memoryCapacity == 0) font.setColor(Color.GRAY);
			batch.draw(memoryRotatedImage, 13, Gdx.graphics.getHeight() - 125);
			font.draw(batch, "" + memoryCapacity, 90, Gdx.graphics.getHeight() - 83);

			// draw battery
			if (batteryLife >= 67) {
				batch.draw(batteryFullImage, 13, Gdx.graphics.getHeight() - 70);
				font.setColor(Color.GREEN);
			}
			else if (batteryLife >= 33) {
				batch.draw(batteryHalfImage, 13, Gdx.graphics.getHeight() - 70);
				font.setColor(Color.YELLOW);

			}
			else if (batteryLife >= 10) {
				batch.draw(batteryLowImage, 13, Gdx.graphics.getHeight() - 70);
				font.setColor(Color.RED);
			}
			else if (batteryLife > 0) {
				batch.draw(batteryVeryLowImage, 13, Gdx.graphics.getHeight() - 70);
				font.setColor(Color.RED);
			}
			else if (batteryLife == 0) {
				batch.draw(batteryDeadImage, 13, Gdx.graphics.getHeight() - 70);
				backgroundMusic.stop();
				font.setColor(Color.RED);
				font.draw(batch, "GAME OVER", Gdx.graphics.getHeight() / 2f - 20, Gdx.graphics.getHeight() / 2f);
			}
			// draw battery life number
			font.draw(batch, "" + batteryLife, 90, Gdx.graphics.getHeight() - 25);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		phoneImage.dispose();
		virusImage.dispose();
		fileImage.dispose();
		folderImage.dispose();
		chargeImage.dispose();
		memoryImage.dispose();
		memoryRotatedImage.dispose();
		batteryFullImage.dispose();
		batteryHalfImage.dispose();
		batteryLowImage.dispose();
		batteryVeryLowImage.dispose();
		batteryDeadImage.dispose();
		wifiBulletImage.dispose();

		filePickUpSound.dispose();
		virusCollisionSound.dispose();
		backgroundMusic.dispose();

		batch.dispose();
		font.dispose();
	}

	private void spawnVirus() {
		Rectangle virus = new Rectangle();
		// spawn virus somewhere random on the top of the window
		virus.x = MathUtils.random(0, Gdx.graphics.getWidth() - virusImage.getWidth());
		virus.y = Gdx.graphics.getHeight();
		virus.width = virusImage.getWidth();
		virus.height = virusImage.getHeight();
		viruses.add(virus);
		lastVirusTime = TimeUtils.nanoTime();
	}

	private void spawnFile() {
		Rectangle file = new Rectangle();
		file.x = MathUtils.random(0, Gdx.graphics.getWidth() - fileImage.getWidth());
		file.y = Gdx.graphics.getHeight();
		file.width = fileImage.getWidth();
		file.height = fileImage.getHeight();
		files.add(file);
		lastFileTime = TimeUtils.nanoTime();
	}

	private void spawnImgFile() {
		Rectangle file = new Rectangle();
		file.x = MathUtils.random(0, Gdx.graphics.getWidth() - imageFileImage.getWidth());
		file.y = Gdx.graphics.getHeight();
		file.width = imageFileImage.getWidth();
		file.height = imageFileImage.getHeight();
		img_files.add(file);
		lastImgFileTime = TimeUtils.nanoTime();
	}

	private void spawnVideoFile() {
		Rectangle file = new Rectangle();
		file.x = MathUtils.random(0, Gdx.graphics.getWidth() - videoFileImage.getWidth());
		file.y = Gdx.graphics.getHeight();
		file.width = videoFileImage.getWidth();
		file.height = videoFileImage.getHeight();
		video_files.add(file);
		lastVideoFileTime = TimeUtils.nanoTime();
	}

	private void spawnMemory() {
		Rectangle memstick = new Rectangle();
		memstick.x = MathUtils.random(0, Gdx.graphics.getWidth() - memoryImage.getWidth());
		memstick.y = Gdx.graphics.getHeight();
		memstick.width = memoryImage.getWidth();
		memstick.height = memoryImage.getHeight();
		memory.add(memstick);
		lastMemoryTime = TimeUtils.nanoTime();
	}

	private void spawnBullet(Rectangle phone) {
		if (TimeUtils.nanoTime() - lastBulletTime > CREATE_BULLET_TIME) {
			Rectangle bullet = new Rectangle();
			// spawn bullet where the phone is
			bullet.x = phone.x;
			bullet.y = phone.y;
			bullet.width = wifiBulletImage.getWidth();
			bullet.height = wifiBulletImage.getHeight();
			bullets.add(bullet);
			if (memoryCapacity > 0) memoryCapacity--;
			lastBulletTime = TimeUtils.nanoTime();
		}
	}

	private void spawnCharge() {
		Rectangle charge = new Rectangle();
		charge.x = MathUtils.random(0, Gdx.graphics.getWidth() - chargeImage.getWidth());
		charge.y = Gdx.graphics.getHeight();
		charge.width = chargeImage.getWidth();
		charge.height = chargeImage.getHeight();
		charges.add(charge);
		lastChargeTime = TimeUtils.nanoTime();
	}

	private void moveLeft() {
		phone.x -= SPEED * Gdx.graphics.getDeltaTime();
		if (phone.x < 0) phone.x = 0;
	}

	private void moveRight() {
		phone.x += SPEED * Gdx.graphics.getDeltaTime();
		if (phone.x > Gdx.graphics.getWidth() - phoneImage.getWidth())
			phone.x = Gdx.graphics.getWidth() - phoneImage.getWidth();
	}

	private void exitGame() {
		Gdx.app.exit();
	}

}
