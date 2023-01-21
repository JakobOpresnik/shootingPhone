package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class Bullet extends DynamicGameObject {
    public int SPEED = 600;
    public static final long CREATE_TIME = 500000000;
    public static long lastSpawnTime;
    public static ParticleEffect particleEffect;

    public Bullet(int x, int y, int width, int height) {
        super(x, y, width, height);
        lastSpawnTime = TimeUtils.nanoTime();

        // setup particle effect
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("bullet_particle_effect.pe"), Gdx.files.internal(""));
        particleEffect.setPosition(x + 15, y);
    }

    public static boolean canCreate() {
        return TimeUtils.nanoTime() - lastSpawnTime > CREATE_TIME;
    }

    @Override
    public void update(float deltaTime) {
        this.bounds.y += SPEED * deltaTime;
        // update particle effect
        particleEffect.setPosition(this.bounds.x + 15, this.bounds.y);
        particleEffect.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.wifiBulletImage, this.bounds.x, this.bounds.y);
        // draw particle effect
        particleEffect.draw(batch);
    }

}
