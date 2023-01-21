package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Score extends GameObject {
    private static int files_downloaded;

    public Score(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public static int getScore() {
        return files_downloaded;
    }

    public static void setScore(int score) {
        files_downloaded = score;
    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.font.setColor(Color.YELLOW);
        Assets.font.draw(batch, "" + files_downloaded, Gdx.graphics.getWidth() - 60, Gdx.graphics.getHeight() - 20);
    }
}
