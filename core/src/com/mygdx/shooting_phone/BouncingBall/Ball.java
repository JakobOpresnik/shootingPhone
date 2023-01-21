package com.mygdx.shooting_phone.BouncingBall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.TimeUtils;


public class Ball {
    public static float g = -1000;
    public float velocity;
    public int x;
    public int y;
    public int radius;
    public Color color;

    public static final int HEIGHT = Gdx.graphics.getHeight();

    public Ball(int x, int y, int radius, float velocity, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocity = velocity;
        this.color = color;
    }

    //public float timeElapsed = TimeUtils.nanoTime();

    public void update(float deltaTime) {
        velocity += g * deltaTime;
        this.y += velocity * deltaTime;
        if (this.y <= radius || this.y > HEIGHT - radius) {
            velocity = -velocity;
            this.y = radius;
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(this.color);
        shape.circle(x, y, radius);
    }
}
