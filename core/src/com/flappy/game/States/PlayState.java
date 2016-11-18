package com.flappy.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.flappy.game.FlappyBird;
import com.flappy.game.Sprites.Bird;
import com.flappy.game.Sprites.Tube;

/**
 * Created by Aryan on 26-10-2016.
 */

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 10;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1,groundPos2;
    private Texture gameOverImg;
    private Array<Tube> tubes;

    private boolean gameOver;
    private static int score;
    private int iterator;

    SpriteBatch batch;
    BitmapFont myTTFont = null;
    private static final String CHAR_STRING="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    protected PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        cam.setToOrtho(false, FlappyBird.WIDTH/2, FlappyBird.HEIGHT/2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        gameOverImg = new Texture("gameover.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2(cam.position.x - cam.viewportWidth/2 + ground.getWidth(), GROUND_Y_OFFSET);
        myTTFont = new BitmapFont();
        FreeTypeFontGenerator generator=new FreeTypeFontGenerator(Gdx.files.internal("comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = CHAR_STRING;
        parameter.color = Color.WHITE;
        parameter.size = 16;
        myTTFont = generator.generateFont(parameter);
        generator.dispose();
        batch = new SpriteBatch();

        tubes = new Array<Tube>();
        for(int i=1;i<=TUBE_COUNT;i++){
            tubes.add(new Tube(i*(TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
        gameOver = false;
        score = 0;
        iterator = 0;
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            if(gameOver)
                gsm.set(new PlayState(gsm));
            else
                bird.jump();
        }
    }

    @Override
    protected void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80;
        for(int i=0;i<tubes.size;i++) {
            Tube tube = tubes.get(i);
            if ((cam.position.x - (cam.viewportWidth / 2)) > tube.getPosTopTube().x + tube.getTopTube().getWidth())
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            if (tube.collide(bird.getBounds())) {
                gameOver = true;
                score = 0;
            }
            if((bird.getPosition().x > tube.getPosTopTube().x + tube.getTopTube().getWidth())&&(iterator%tubes.size==i)) {
                score += 1;
                iterator = i+1;
            }
            System.out.println("Score: "+score);
        }
        if(bird.getPosition().y <= ground.getHeight()+GROUND_Y_OFFSET) {
            gameOver = true;
            score = 0;
        }
        cam.update();
        System.out.println("Score: "+score);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth/2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for(Tube tube:tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        myTTFont.draw(sb, String.valueOf(score), cam.position.x, 2*cam.position.y-cam.position.y/32);
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        if(gameOver)
            sb.draw(gameOverImg, cam.position.x - gameOverImg.getWidth() / 2, cam.position.y);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        for(Tube tube:tubes){
            tube.dispose();
        }
        System.out.println("Play State disposed");
    }

    private void updateGround(){
        if((cam.position.x - cam.viewportWidth/2)>groundPos1.x+ground.getWidth())
            groundPos1.add(ground.getWidth() * 2,0);
        if((cam.position.x - cam.viewportWidth/2)>groundPos2.x+ground.getWidth())
            groundPos2.add(ground.getWidth() * 2,0);
    }
}
