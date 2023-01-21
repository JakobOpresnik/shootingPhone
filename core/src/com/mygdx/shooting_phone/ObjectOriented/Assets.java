package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Assets {
    public static Texture backgroundImage;
    public static Texture phoneImage;
    public static Texture virusImage;
    public static Texture fileImage;
    public static Texture imageFileImage;
    public static Texture videoFileImage;
    public static Texture folderImage;
    public static Texture chargeImage;
    public static Texture memoryImage;
    public static Texture memoryRotatedImage;
    public static Texture batteryFullImage;
    public static Texture batteryHalfImage;
    public static Texture batteryLowImage;
    public static Texture batteryVeryLowImage;
    public static Texture batteryDeadImage;
    public static Texture wifiBulletImage;
    public static Texture antivirusImage;
    public static Texture antivirusImageSmall;

    public static Sound filePickUpSound;
    public static Sound virusCollisionSound;
    public static Sound memoryPickUpSound;
    public static Sound chargePickUpSound;
    public static Music backgroundMusic;

    public static BitmapFont font;


    public static Texture loadTexture(String filename) {
        return new Texture(Gdx.files.internal(filename));
    }

    public static Sound loadSound(String filename) {
        return Gdx.audio.newSound(Gdx.files.internal(filename));
    }

    public static Music loadMusic(String filename) {
        return Gdx.audio.newMusic(Gdx.files.internal(filename));
    }

    public static void playMusic(Music music, float volume) {
        backgroundMusic.setLooping(true);
        music.setVolume(volume);
        music.play();
    }

    public static BitmapFont loadFont(String filename, int font_size, int font_border_width) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator((Gdx.files.internal(filename)));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = font_size;
        fontParameter.borderWidth = font_border_width;
        return fontGenerator.generateFont(fontParameter);
    }

    public static void load() {
        backgroundImage = loadTexture("background.png");
        phoneImage = loadTexture("phone.png");
        virusImage = loadTexture("virus.png");
        fileImage = loadTexture("text_file.png");
        imageFileImage = loadTexture("image_file.png");
        videoFileImage = loadTexture("video_file.png");
        folderImage = loadTexture("folder.png");
        chargeImage = loadTexture("charge.png");
        memoryImage = loadTexture("memory.png");
        batteryFullImage = loadTexture("battery_full.png");
        batteryHalfImage = loadTexture("battery_half.png");
        batteryLowImage = loadTexture("battery_low.png");
        batteryVeryLowImage = loadTexture("battery_really_low.png");
        batteryDeadImage = loadTexture("battery_dead.png");
        memoryRotatedImage = loadTexture("memory_rotated.png");
        wifiBulletImage = loadTexture("wifi.png");
        antivirusImage = loadTexture("antivirus.png");
        antivirusImageSmall = loadTexture("antivirus_small.png");

        filePickUpSound = loadSound("pop.mp3");
        virusCollisionSound = loadSound("virus_collision.mp3");
        memoryPickUpSound = loadSound("power_up.wav");
        chargePickUpSound = loadSound("heal.wav");

        backgroundMusic = loadMusic("background_music.wav");
        playMusic(backgroundMusic, 0.2f);

        font = loadFont("death_star_font.otf", 25, 2);
    }

    public static void dispose() {
        phoneImage.dispose();
        virusImage.dispose();
        fileImage.dispose();
        imageFileImage.dispose();
        videoFileImage.dispose();
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
        antivirusImage.dispose();
        antivirusImageSmall.dispose();

        filePickUpSound.dispose();
        virusCollisionSound.dispose();
        memoryPickUpSound.dispose();
        chargePickUpSound.dispose();

        backgroundMusic.dispose();

        font.dispose();
    }
}
