package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.math.Vector2;

abstract class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 acceleration;

    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2();
        acceleration = new Vector2();
    }

    abstract void update(float deltaTime);

}
