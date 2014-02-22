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
import frontline.model.gfx.ui.OptionsUI;
import frontline.model.util.OptionsPacket;
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
public class MenuScene extends GameScene {

    public static final int BACKGROUND = 0;
    public static final int OPTIONSUI = 1;
    public static final int RETICULE = 2;
    
    public HashMap<Integer, Component> components;
    public static ResourceLoader depot;

    public Sprite reticule;
    public OptionsUI options;
    
    public MenuScene() {
        type = Scene.MENU;
        components = new HashMap<Integer, Component>();
        initComponents();
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

    public void initComponents() {
        depot = new ResourceLoader();
        components.put(BACKGROUND, new Background("options"));
        options = new OptionsUI();
        components.put(OPTIONSUI, options);
        HashMap<String, Animation> rets = new HashMap<String, Animation>();
        rets.put("unshot", new Animation(MenuScene.depot.ui.get("unshot"), 2, 200));
        rets.put("shot", new Animation(MenuScene.depot.ui.get("shot"), 2, 200));
        reticule = new Sprite(rets);
        reticule.hud = true;
        reticule.animation = reticule.animations.get("unshot");
        components.put(RETICULE, reticule);
    }
    
    public OptionsPacket getOptions(){
        return options.getOptions();
    }
}
