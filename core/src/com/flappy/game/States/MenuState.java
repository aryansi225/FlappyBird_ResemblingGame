package com.flappy.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.flappy.game.FlappyBird;

/**
 * Created by Aryan on 26-10-2016.
 */

public class MenuState extends State {
    private Texture background;
    private Texture playBtn;
    private Rectangle play;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, FlappyBird.WIDTH/2, FlappyBird.HEIGHT/2);
        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
        play = new Rectangle(cam.position.x-playBtn.getWidth()/2, cam.position.y, playBtn.getWidth(), playBtn.getHeight());
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            cam.unproject(touchPos);
            if(play.contains(touchPos.x,touchPos.y))
                gsm.set(new PlayState(gsm));
        }
    }

    @Override
    protected void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //sb.draw(background, 0, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);
        sb.draw(background, 0, 0);
        //sb.draw(playBtn, (FlappyBird.WIDTH/2)-(playBtn.getWidth()/2),FlappyBird.HEIGHT/2);
        sb.draw(playBtn, cam.position.x-playBtn.getWidth()/2, cam.position.y);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        System.out.println("Menu State disposed");
    }
}
