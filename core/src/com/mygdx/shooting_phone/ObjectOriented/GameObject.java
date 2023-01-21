package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

abstract class GameObject {
    public final Rectangle bounds;

    public GameObject(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    abstract void render(SpriteBatch batch);
}
