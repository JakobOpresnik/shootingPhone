package com.mygdx.shooting_phone.BouncingBall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class BouncingBall extends ApplicationAdapter {
    ShapeRenderer shape;
    Ball ball;
    Array<Ball> balls = new Array<>();

    @Override
    public void create() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shape = new ShapeRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b, 100);

            int randomRadius = rand.nextInt(50) + 20;

            // spawn new ball
            ball = new Ball(Gdx.input.getX(), Ball.HEIGHT - Gdx.input.getY(), randomRadius, 0, randomColor);
            balls.add(ball);

            // draw all existing balls
            for (Ball ball : balls) {
                ball.update(Gdx.graphics.getDeltaTime());
                shape.begin(ShapeRenderer.ShapeType.Filled);
                ball.draw(shape);
                shape.end();
            }
        }

        // draw all balls
        for (Ball ball : balls) {
           ball.update(Gdx.graphics.getDeltaTime());
           shape.begin(ShapeRenderer.ShapeType.Filled);
           ball.draw(shape);
           shape.end();
       }

    }
}

