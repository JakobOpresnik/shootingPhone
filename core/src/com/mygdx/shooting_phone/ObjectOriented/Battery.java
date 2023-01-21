package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Battery extends GameObject {
    private static int health = 100;    // start wit 100% battery

    public Battery(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public static int getHealth() {
        return health;
    }

    public static void setHealth(int new_health) {
        health = new_health;
    }

    public static void decreaseHealth() {
        health--;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (health >= 67) {
            batch.draw(Assets.batteryFullImage, 13, Gdx.graphics.getHeight() - 70);
            Assets.font.setColor(Color.GREEN);
        }
        else if (health >= 33) {
            batch.draw(Assets.batteryHalfImage, 13, Gdx.graphics.getHeight() - 70);
            Assets.font.setColor(Color.YELLOW);
        }
        else if (health >= 10) {
            batch.draw(Assets.batteryLowImage, 13, Gdx.graphics.getHeight() - 70);
            Assets.font.setColor(Color.RED);
        }
        else if (health > 0) {
            batch.draw(Assets.batteryVeryLowImage, 13, Gdx.graphics.getHeight() - 70);
            Assets.font.setColor(Color.RED);
        }
        else if (health == 0) {
            batch.draw(Assets.batteryDeadImage, 13, Gdx.graphics.getHeight() - 70);
            Assets.font.setColor(Color.RED);
            Assets.font.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2f - 80, Gdx.graphics.getHeight() / 2f);
        }
        Assets.font.draw(batch, "" + health, 90, Gdx.graphics.getHeight() - 25);
    }

    public static boolean isGameOver() {
        return health <= 0;
    }
}
