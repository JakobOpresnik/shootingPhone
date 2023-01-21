package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class MyPhone extends DynamicGameObject {
    public final int SPEED = 600;
    public static long lastSpawnTime;
    public static final int MOVE_LEFT = -1;
    public static final int MOVE_RIGHT = 1;
    public static final int NO_MOVE = 0;
    private int move_state;
    public static boolean has_antivirus;
    public static ParticleEffect particleEffect;

    public MyPhone(int x, int y, int width, int height) {
        super(x, y, width, height);
        lastSpawnTime = TimeUtils.nanoTime();
        move_state = NO_MOVE;
        has_antivirus = false;

        // setup particle effect
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("phone_particle_effect.pe"), Gdx.files.internal(""));
        particleEffect.setPosition(x + 22, y - 12);
    }

    public void setMoveLeft() {
        move_state = MOVE_LEFT;
    }

    public void setMoveRight() {
        move_state = MOVE_RIGHT;
    }

    public void render(SpriteBatch batch) {
        batch.draw(Assets.phoneImage, this.bounds.x, this.bounds.y);
        // draw particle effect
        particleEffect.draw(batch);
    }

    @Override
    void update(float deltaTime) {
        this.bounds.x += SPEED * deltaTime * move_state;
        if (this.bounds.x < 0) this.bounds.x = 0;
        if (this.bounds.x > Gdx.graphics.getWidth() - Assets.phoneImage.getWidth() - 20) {
            this.bounds.x = Gdx.graphics.getWidth() - Assets.phoneImage.getWidth() - 20;
        }
        move_state = NO_MOVE;

        // update particle effect
        particleEffect.setPosition(this.bounds.x + 22, this.bounds.y - 12);
        particleEffect.update(deltaTime);
    }
}
