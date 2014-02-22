/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontline.model;

import Resources.ResourceLoader;
import frontline.DeathMatch.Scene;
import frontline.model.gfx.Background;
import frontline.model.gfx.core.Animation;
import frontline.model.gfx.core.Component;
import frontline.model.gfx.core.Sprite;
import frontline.model.util.Services;

import java.awt.Graphics2D;
import java.util.HashMap;

/** Copyright 2011, Bleep Bloop Software.
 *  Unpublished Copyright, All Rights Reserved
 *
 */
/**
 *
 * @author Nick
 */
public class IntroScene extends GameScene {

    public static final int BACKGROUND = 0;
    public static final int PRESSENTER = 1;
    public HashMap<Integer, Component> components;
    public static ResourceLoader depot;

    public IntroScene() {
        type = Scene.INTRO;
        components = new HashMap<Integer, Component>();
        initComponents();
    }

    public void initComponents() {
        depot = new ResourceLoader();
        components.put(BACKGROUND, new Background("opening"));
        Animation anim = new Animation(depot.ui.get("PressEnter"), 2, 500);
        Sprite pe = new Sprite(anim);
        pe.position.X = (Services.screenWidth - pe.getWidth())/2;
        pe.position.Y = 475;
        components.put(PRESSENTER, pe);
    }

    @Override
    public void draw(Graphics2D g) {
        Integer[] keys = components.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            components.get(keys[i]).draw(g);
        }
    }

    @Override
    public void update(long timePassed) {
        Integer[] keys = components.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            components.get(keys[i]).update(timePassed);
        }
    }
}
