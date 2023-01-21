package com.mygdx.shooting_phone.ObjectOriented;

import static jdk.internal.vm.compiler.word.LocationIdentity.init;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;

public class Virus extends DynamicGameObject implements Pool.Poolable {
    public static int SPEED = 200;
    public static final long CREATE_TIME = 700000000;
    public static long lastSpawnTime;

    //public static final Pool<Virus> virusPool = Pools.get(Virus.class, 50);
    public static final Pool<Virus> virusPool = new Pool<Virus>(1, 5) {
        @Override
        protected Virus newObject() {
            return new Virus(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.virusImage.getWidth()), Gdx.graphics.getHeight(), Assets.virusImage.getWidth(), Assets.virusImage.getHeight());
        }
    };


    public Virus(int x, int y, int width, int height) {
        super(x, y, width, height);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    public static boolean canCreate() {
        return TimeUtils.nanoTime() - lastSpawnTime > CREATE_TIME;
    }

    @Override
    public void update(float deltaTime) {
        this.bounds.y -= SPEED * deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.virusImage, this.bounds.x, this.bounds.y);
    }

    // called when object is freed (returned to the pool)
    @Override
    public void reset() {
        init();
    }

    // initialized object returned to the pool
    public void init() {
        this.bounds.set(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.virusImage.getWidth()), Gdx.graphics.getHeight(), Assets.virusImage.getWidth(), Assets.virusImage.getHeight());
        lastSpawnTime = TimeUtils.nanoTime();
    }

}
