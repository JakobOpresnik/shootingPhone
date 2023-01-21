package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.shooting_phone.ObjectOriented.DebugUtils.ViewportUtils;
import com.mygdx.shooting_phone.ObjectOriented.DebugUtils.debug.DebugCameraController;
import com.mygdx.shooting_phone.ObjectOriented.DebugUtils.debug.DebugCameraInfo;
import com.mygdx.shooting_phone.ObjectOriented.DebugUtils.debug.MemoryInfo;

import java.sql.Time;
import java.util.Iterator;

public class ShootingPhoneOOP extends ApplicationAdapter {

    public SpriteBatch batch;
    public OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Viewport viewPort;
    private Viewport hudViewPort;
    private DebugCameraController debugCameraController;
    private boolean debug = false;
    private MemoryInfo memoryInfo;

    private static MyPhone phone;
    int WIDTH, HEIGHT;
    public boolean paused = false;
    public long antivirusReceived;

    private Array<Virus> active_viruses;
    private Array<TextFile> active_files;
    private Array<ImageFile> active_img_files;
    private Array<VideoFile> active_video_files;
    private Array<Memory> active_memory;
    private Array<Charge> charges;
    private Array<Bullet> active_bullets;
    private Array<Antivirus> antiviruses;

    private static final float WORLD_WIDTH = 620;
    private static final float WORLD_HEIGHT = 480;

    //private final Array<Virus> activeViruses = new Array<>();
    //private final Pool<Virus> virusPool = Pools.get(Virus.class, 50);

    // runs at start of the game
    public void create() {
        batch = new SpriteBatch();
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        // load all textures
        Assets.load();

        // setup cam
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        shapeRenderer = new ShapeRenderer();
        viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        hudViewPort = new FitViewport(WIDTH, HEIGHT);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);

        memoryInfo = new MemoryInfo(1000);

        active_viruses = new Array<>();
        active_files = new Array<>();
        active_img_files = new Array<>();
        active_video_files = new Array<>();
        active_memory = new Array<>();
        active_bullets = new Array<>();
        charges = new Array<>();
        antiviruses = new Array<>();

        phone = new MyPhone((int)(WORLD_WIDTH / 2f), 120, Assets.phoneImage.getWidth(), Assets.phoneImage.getHeight());

        spawnVirus();
        spawnFile();
        spawnImageFile();
        spawnVideoFile();
        spawnMemory();
        spawnCharge();
        spawnAntivirus();
    }


    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
        hudViewPort.update(width, height, true);
        ViewportUtils.debugPixelsPerUnit(viewPort);
    }


    // runs every frame
    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // draw background
        batch.draw(Assets.backgroundImage, 0, 0);
        batch.end();

        //camera.update();
        //batch.setProjectionMatrix(camera.combined);
        //viewPort.apply();
        //batch.setProjectionMatrix(viewPort.getCamera().combined);


        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) debug = !debug;

        if (debug) {
            debugCameraController.handleDebugInput(Gdx.graphics.getDeltaTime());
            debugCameraController.applyTo(camera);
        }


        if (!paused) {
            Assets.backgroundMusic.play();

            // spawn game objects
            if (Virus.canCreate()) spawnVirus();
            if (TextFile.canCreate()) spawnFile();
            if (ImageFile.canCreate()) spawnImageFile();
            if (VideoFile.canCreate()) spawnVideoFile();
            if (Memory.canCreate()) spawnMemory();
            if (Charge.canCreate()) spawnCharge();
            if (Antivirus.canCreate()) spawnAntivirus();


            if (!Battery.isGameOver()) {

                if (isAntivirusOver()) {
                    MyPhone.has_antivirus = false;
                }

                // check for user input
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    phone.setMoveLeft();
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    phone.setMoveRight();
                if (MemoryScore.getMemoryScore() > 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE))
                    shoot(phone);
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) paused = true;
                if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) exitGame();

                phone.update(Gdx.graphics.getDeltaTime());

                // update game objects' positions
                for (Virus virus : active_viruses) {
                    virus.update(Gdx.graphics.getDeltaTime());
                    // if virus goes offscreen
                    if (virus.bounds.y < 0) {
                        //System.out.println("size=" + active_viruses.size);
                        // remove from array of active viruses
                        active_viruses.removeValue(virus, true);
                        // return virus back to the pool
                        Virus.virusPool.free(virus);
                    }
                }

                for (TextFile file : active_files) {
                    file.update(Gdx.graphics.getDeltaTime());
                    if (file.bounds.y < 0) {
                        active_files.removeValue(file, true);
                        TextFile.filePool.free(file);
                    }
                }

                for (ImageFile img_file : active_img_files) {
                    img_file.update(Gdx.graphics.getDeltaTime());
                    if (img_file.bounds.y < 0) {
                        active_img_files.removeValue(img_file, true);
                        ImageFile.imgfilePool.free(img_file);
                    }
                }

                for (VideoFile video_file : active_video_files) {
                    video_file.update(Gdx.graphics.getDeltaTime());
                    if (video_file.bounds.y < 0) {
                        active_video_files.removeValue(video_file, true);
                        VideoFile.videofilePool.free(video_file);
                    }
                }

                for (Memory mem_stick : active_memory) {
                    mem_stick.update(Gdx.graphics.getDeltaTime());
                    if (mem_stick.bounds.y < 0) {
                        active_memory.removeValue(mem_stick, true);
                        Memory.memoryPool.free(mem_stick);
                    }
                }

                for (Charge charge : charges) {
                    charge.update(Gdx.graphics.getDeltaTime());
                }

                for (Bullet bullet : active_bullets) {
                    bullet.update(Gdx.graphics.getDeltaTime());
                }

                for (Antivirus antivirus : antiviruses) {
                    antivirus.update(Gdx.graphics.getDeltaTime());
                }

                // check for collisions
                checkVirusCollision();
                checkFileCollision();
                checkImageFileCollision();
                checkVideoFileCollision();
                checkMemoryCollision();
                checkBulletVirusCollision();
                checkChargeCollision();
                checkAntivirusCollision();
            }
            else {
                // stop music
                Assets.backgroundMusic.stop();
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    dispose();
                    create();
                    Battery.setHealth(100);
                    Score.setScore(0);
                }
            }
        }
        else {
            // pause music
            Assets.backgroundMusic.pause();
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                paused = false;
                //return;
            }
        }


        Score score = displayScore();
        Battery health = displayHealth();
        MemoryScore mem_score = displayMemoryScore();


        //hudViewPort.apply();
        batch.setProjectionMatrix(camera.combined);

        // render game objects
        batch.begin();
        {

            // draw phone
            phone.render(batch);
            if (MyPhone.has_antivirus && !isAntivirusOver()) {
                batch.draw(Assets.antivirusImageSmall, phone.bounds.x + Assets.phoneImage.getWidth() / 2f, phone.bounds.y + Assets.phoneImage.getHeight() - 20);
            }

            // draw viruses
            for (Virus virus : active_viruses) {
                virus.render(batch);
            }

            // draw text files
            for (TextFile file : active_files) {
                file.render(batch);
            }

            // draw image files
            for (ImageFile img_file : active_img_files) {
                img_file.render(batch);
            }

            // draw video files
            for (VideoFile video_file : active_video_files) {
                video_file.render(batch);
            }

            // draw memory
            for (Memory mem_stick : active_memory) {
                mem_stick.render(batch);
            }

            // draw charges
            for (Charge charge : charges) {
                charge.render(batch);
            }

            // draw bullets
            for (Bullet bullet : active_bullets) {
                bullet.render(batch);
            }

            // draw antiviruses
            for (Antivirus antivirus : antiviruses) {
                antivirus.render(batch);
            }

            // display score
            score.render(batch);

            // display health (battery)
            health.render(batch);

            // display memory score
            mem_score.render(batch);

            if (paused) {
                Pause pause = pauseMenu();
                pause.render(batch);
            }
        }
        batch.end();

        // debug screen
        if (debug) {
            renderDebug();
            batch.begin();
            draw();
            // draw memory info
            memoryInfo.render(batch, Assets.font);
            batch.end();
        }
    }

    // draw screen & world unit details
    private void draw() {
        int screenWidth = WIDTH;
        int screenHeight = HEIGHT;
        float worldWidth = WORLD_WIDTH;
        float worldHeight = WORLD_HEIGHT;

        String screenSize = "Screen/Window size: " + screenWidth + " x " + screenHeight + " px";
        String worldSize = "World size: " + (int) worldWidth + " x " + (int) worldHeight + " world units";
        String oneWorldUnit = "One world unit: " + (screenWidth / worldWidth) + " x " + (screenHeight / worldHeight) + " px";

        Assets.font.draw(batch,
                screenSize,
                20f,
                HEIGHT - 20f);

        Assets.font.draw(batch,
                worldSize,
                20f,
                HEIGHT - 50f);

        Assets.font.draw(batch,
                oneWorldUnit,
                20f,
                HEIGHT - 80f);
    }

    // draw grid
    private void renderDebug() {
        ViewportUtils.drawGrid(viewPort, shapeRenderer, 30);
        viewPort.apply();

        Color oldColor = new Color(shapeRenderer.getColor());
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        shapeRenderer.end();
        shapeRenderer.setColor(oldColor);
    }

    // draw rectangles around objects
    private void drawDebug() {
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(phone.bounds.x, phone.bounds.y, phone.bounds.width, phone.bounds.height);
        for (Virus virus : active_viruses) {
            shapeRenderer.rect(virus.bounds.x, virus.bounds.y, virus.bounds.width, virus.bounds.height);
        }
        for (TextFile textfile : active_files) {
            shapeRenderer.rect(textfile.bounds.x, textfile.bounds.y, textfile.bounds.width, textfile.bounds.height);
        }
        for (ImageFile imageFile : active_img_files) {
            shapeRenderer.rect(imageFile.bounds.x, imageFile.bounds.y, imageFile.bounds.width, imageFile.bounds.height);
        }
        for (VideoFile videoFile : active_video_files) {
            shapeRenderer.rect(videoFile.bounds.x, videoFile.bounds.y, videoFile.bounds.width, videoFile.bounds.height);
        }
        for (Memory mem_stick : active_memory) {
            shapeRenderer.rect(mem_stick.bounds.x, mem_stick.bounds.y, mem_stick.bounds.width, mem_stick.bounds.height);
        }
        for (Charge charge : charges) {
            shapeRenderer.rect(charge.bounds.x, charge.bounds.y, charge.bounds.width, charge.bounds.height);
        }
        for (Bullet bullet : active_bullets) {
            shapeRenderer.rect(bullet.bounds.x, bullet.bounds.y, bullet.bounds.width, bullet.bounds.height);
        }
        for (Antivirus antivirus : antiviruses) {
            shapeRenderer.rect(antivirus.bounds.x, antivirus.bounds.y, antivirus.bounds.width, antivirus.bounds.height);
        }
    }

    private void spawnVirus() {
        //Virus virus = new Virus(MathUtils.random(0, WIDTH - Assets.virusImage.getWidth()), HEIGHT, Assets.virusImage.getWidth(), Assets.virusImage.getHeight());

        // get object from pool
        Virus virus = Virus.virusPool.obtain();
        // initialize object
        virus.init();
        // add to array
        active_viruses.add(virus);
    }

    private void spawnFile() {
        TextFile file = TextFile.filePool.obtain();
        file.init();
        active_files.add(file);
    }

    private void spawnImageFile() {
        ImageFile img_file = ImageFile.imgfilePool.obtain();
        img_file.init();
        active_img_files.add(img_file);
    }

    private void spawnVideoFile() {
        VideoFile video_file = VideoFile.videofilePool.obtain();
        video_file.init();
        active_video_files.add(video_file);
    }

    private void spawnMemory() {
        Memory mem_stick = Memory.memoryPool.obtain();
        mem_stick.init();
        active_memory.add(mem_stick);
    }

    private void spawnCharge() {
        Charge charge = new Charge(MathUtils.random(0, (int)WORLD_WIDTH - Assets.chargeImage.getWidth()), (int)WORLD_HEIGHT, Assets.chargeImage.getWidth(), Assets.chargeImage.getHeight());
        charges.add(charge);
    }

    private void shoot(MyPhone phone) {
        if (Bullet.canCreate()) {
            Bullet bullet = new Bullet((int)phone.bounds.x + Assets.phoneImage.getWidth()/2, (int) phone.bounds.y + Assets.phoneImage.getHeight(), Assets.wifiBulletImage.getWidth(), Assets.wifiBulletImage.getHeight());
            active_bullets.add(bullet);
            if (MemoryScore.getMemoryScore() > 0) MemoryScore.decreaseAmmo();
        }
    }

    private void spawnAntivirus() {
        Antivirus antivirus = new Antivirus(MathUtils.random(0, (int)WORLD_WIDTH - Assets.antivirusImage.getWidth()), (int)WORLD_HEIGHT, Assets.antivirusImage.getWidth(), Assets.antivirusImage.getHeight());
        antiviruses.add(antivirus);
    }

    private Score displayScore() {
        return new Score((int)WORLD_WIDTH - 60, (int)WORLD_HEIGHT - 20, 20, 10);
    }

    private Battery displayHealth() {
        return new Battery(13, (int)WORLD_HEIGHT - 70, Assets.batteryFullImage.getWidth(), Assets.batteryFullImage.getHeight());
    }

    private MemoryScore displayMemoryScore() {
        return new MemoryScore(13, (int)WORLD_HEIGHT - 125, Assets.memoryRotatedImage.getWidth(), Assets.memoryRotatedImage.getHeight());
    }

    private Pause pauseMenu() {
        return new Pause((int) (WORLD_WIDTH / 2f + 20), (int) (WORLD_HEIGHT / 2f), 20, 10);
    }

    private void checkVirusCollision() {
        for (Iterator<Virus> it = active_viruses.iterator(); it.hasNext();) {
            Virus virus = it.next();
            if (virus.bounds.y + Assets.virusImage.getHeight() < 0) it.remove();
            if (virus.bounds.overlaps(phone.bounds) && !MyPhone.has_antivirus) {
                Assets.virusCollisionSound.play(0.2f);
                Battery.decreaseHealth();
            }
        }
    }

    private void checkFileCollision() {
        for (Iterator<TextFile> it = active_files.iterator(); it.hasNext();) {
            TextFile file = it.next();
            if (file.bounds.y + Assets.fileImage.getHeight() < 0) it.remove();
            if (file.bounds.overlaps(phone.bounds)) {
                Assets.filePickUpSound.play(0.2f);
                Score.setScore(Score.getScore() + 1);
                if (Score.getScore() % 10 == 0) Virus.SPEED += 50;
                it.remove();
            }
        }
    }

    private void checkImageFileCollision() {
        for (Iterator<ImageFile> it = active_img_files.iterator(); it.hasNext();) {
            ImageFile img_file = it.next();
            if (img_file.bounds.y + Assets.imageFileImage.getHeight() < 0) it.remove();
            if (img_file.bounds.overlaps(phone.bounds)) {
                Assets.filePickUpSound.play(0.25f);
                Score.setScore(Score.getScore() + 2);
                if (Score.getScore() % 10 == 0) Virus.SPEED += 50;
                it.remove();
            }
        }
    }

    private void checkVideoFileCollision() {
        for (Iterator<VideoFile> it = active_video_files.iterator(); it.hasNext();) {
            VideoFile video_file = it.next();
            if (video_file.bounds.y + Assets.videoFileImage.getHeight() < 0) it.remove();
            if (video_file.bounds.overlaps(phone.bounds)) {
                Assets.filePickUpSound.play(0.25f);
                Score.setScore(Score.getScore() + 3);
                if (Score.getScore() % 10 == 0) Virus.SPEED += 50;
                it.remove();
            }
        }
    }

    private void checkMemoryCollision() {
        for (Iterator<Memory> it = active_memory.iterator(); it.hasNext();) {
            Memory mem_stick = it.next();
            if (mem_stick.bounds.y + Assets.memoryImage.getHeight() < 0) it.remove();
            if (mem_stick.bounds.overlaps(phone.bounds)) {
                if (MemoryScore.getMemoryScore() < 10) {
                    Assets.memoryPickUpSound.play(0.2f);
                    MemoryScore.setMemoryScore(MemoryScore.getMemoryScore() + 1);
                    it.remove();
                }
            }
        }
    }

    private void checkBulletVirusCollision() {
        for (Iterator<Bullet> it = active_bullets.iterator(); it.hasNext();) {
            Bullet bullet = it.next();
            if (bullet.bounds.y + Assets.wifiBulletImage.getHeight() > WORLD_HEIGHT) it.remove();
            for (Iterator<Virus> itv = active_viruses.iterator(); itv.hasNext();) {
                Virus virus = itv.next();
                if (bullet.bounds.overlaps(virus.bounds)) {
                    it.remove();
                    itv.remove();
                }
            }
        }
    }

    private void checkChargeCollision() {
        for (Iterator<Charge> it = charges.iterator(); it.hasNext();) {
            Charge charge = it.next();
            if (charge.bounds.y + Assets.chargeImage.getHeight() < 0) it.remove();
            if (charge.bounds.overlaps(phone.bounds)) {
                if (Battery.getHealth() <= 80) {
                    Assets.chargePickUpSound.play(0.2f);
                    Battery.setHealth(Battery.getHealth() + 20);
                    it.remove();
                }
            }
        }
    }

    private void checkAntivirusCollision() {
        for (Iterator<Antivirus> it = antiviruses.iterator(); it.hasNext();) {
            Antivirus antivirus = it.next();
            if (antivirus.bounds.y + Assets.antivirusImage.getHeight() < 0) it.remove();
            if (antivirus.bounds.overlaps(phone.bounds) && isAntivirusOver()) {
                MyPhone.has_antivirus = true;
                antivirusReceived = TimeUtils.nanoTime();
                it.remove();
            }
        }
    }

    private boolean isAntivirusOver() {
        return TimeUtils.nanoTime() - antivirusReceived > (5L *1000000000);
    }

    public void dispose() {
        Assets.phoneImage.dispose();
        Assets.virusImage.dispose();
        Assets.fileImage.dispose();
        Assets.folderImage.dispose();
        Assets.chargeImage.dispose();
        Assets.memoryImage.dispose();
        Assets.memoryRotatedImage.dispose();
        Assets.batteryFullImage.dispose();
        Assets.batteryHalfImage.dispose();
        Assets.batteryLowImage.dispose();
        Assets.batteryVeryLowImage.dispose();
        Assets.batteryDeadImage.dispose();
        Assets.wifiBulletImage.dispose();
        Assets.antivirusImage.dispose();
        Assets.antivirusImageSmall.dispose();

        Assets.filePickUpSound.dispose();
        Assets.virusCollisionSound.dispose();
        Assets.backgroundMusic.dispose();

        batch.dispose();
        shapeRenderer.dispose();
        Assets.font.dispose();
    }

    private void exitGame() {
        Gdx.app.exit();
    }
}
