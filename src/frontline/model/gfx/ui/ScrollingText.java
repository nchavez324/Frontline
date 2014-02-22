/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontline.model.gfx.ui;

import frontline.DeathMatch.Scene;
import frontline.model.gfx.core.Component;
import frontline.model.util.Services;
import frontline.model.util.Vector2;
import java.awt.Color;
import java.awt.Graphics2D;

/** Copyright 2011, Bleep Bloop Software.
 *  Unpublished Copyright, All Rights Reserved
 *
 */
/**
 *
 * @author Nick
 */
public class ScrollingText implements Component{

    public String message;
    public float velocity;
    public Vector2 position;
    public boolean visible;
    
    public ScrollingText(String message){
        this.message = message;
        loadResources();
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawString(message, position.X, position.Y);
    }

    @Override
    public void update(long timePassed) {
        position.Y += velocity * timePassed;
        if(position.Y <= 0){
        	Services.deathmatch.switchScenes(Scene.INTRO);
        }
    }

    @Override
    public void loadResources() {
        velocity = -.1f;
        position = new Vector2(350, Services.screenHeight + Services.topInset);
    }
    
}
