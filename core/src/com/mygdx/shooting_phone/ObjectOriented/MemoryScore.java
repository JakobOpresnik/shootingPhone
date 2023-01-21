package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MemoryScore extends GameObject {
    private static int capacity;

    public MemoryScore(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public static int getMemoryScore() {
        return capacity;
    }

    public static void setMemoryScore(int score) {
        capacity = score;
    }

    public static void decreaseAmmo() {
        capacity--;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (capacity >= 10) {
            Assets.font.setColor(Color.RED);
            Assets.font.draw(batch, "MEMORY FULL", Gdx.graphics.getHeight() / 2f - 30, Gdx.graphics.getHeight() / 2f);
        }
        else if (capacity > 0 && capacity <= 7) Assets.font.setColor(Color.ORANGE);
        else if (capacity > 7) Assets.font.setColor(Color.RED);
        else if (capacity == 0) Assets.font.setColor(Color.GRAY);
        batch.draw(Assets.memoryRotatedImage, 13, Gdx.graphics.getHeight() - 125);
        Assets.font.draw(batch, "" + capacity, 90, Gdx.graphics.getHeight() - 83);
    }

}
