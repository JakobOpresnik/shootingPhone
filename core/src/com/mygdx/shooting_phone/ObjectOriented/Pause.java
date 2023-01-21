package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pause extends GameObject {

    public Pause(int x, int y, int width, int height) { super(x, y, width, height); }

    @Override
    public void render(SpriteBatch batch) {
        Assets.font.setColor(Color.YELLOW);
        Assets.font.draw(batch, "PAUSED", Gdx.graphics.getWidth() / 2f - 70, Gdx.graphics.getHeight() / 2f + 30);
        Assets.font.draw(batch, "Press 'P' to resume", Gdx.graphics.getWidth() / 2f - 170, Gdx.graphics.getHeight() / 2f - 10);
    }
}
