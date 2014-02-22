/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package frontline.controller;

import java.awt.event.MouseEvent;

import frontline.DeathMatch.Scene;
import frontline.model.ActionScene;
import frontline.model.GameScene;
import frontline.model.IntroScene;
import frontline.model.MenuScene;
import frontline.model.gfx.Fighter.SoldierClass;
import frontline.model.gfx.Weapon;
import frontline.model.gfx.ui.Button;
import frontline.model.util.Services;
import frontline.model.util.Vector2;

/**
 * TODO: type comment.
 *
 * @version Aug 17, 2011, submitted by Nick Chavez
 */
public class MouseManager {

    private GameScene engine;
    @SuppressWarnings("unused")
	private IntroScene introengine;
    private MenuScene menuengine;
    private ActionScene actionengine;
    public static final int MOUSE_RELEASED = 0;
    public static final int MOUSE_PRESSED = 1;
    public int oldState;
    public int currentState;

    public MouseManager(GameScene engine) {
        this.engine = engine;
        if (engine.type == Scene.ACTION) {
            actionengine = (ActionScene) engine;
        } else if (engine.type == Scene.INTRO) {
            introengine = (IntroScene) engine;
        }
    }

    public void switchScenes(GameScene engine) {
        this.engine = engine;
        if (engine.type == Scene.ACTION) {
            actionengine = (ActionScene) engine;
        } else if (engine.type == Scene.INTRO) {
            introengine = (IntroScene) engine;
        } else if (engine.type == Scene.MENU) {
            menuengine = (MenuScene) engine;
        }
    }

    public void handleMouseMoved(int x, int y) {
        if (engine.type == Scene.ACTION) {
            double angle = Services.getAngle(actionengine.player.position.X + actionengine.player.getWidth() / 2 - Services.camera.x,
                    actionengine.player.position.Y + actionengine.player.getHeight() / 2 - Services.camera.y,
                    x, y);
            actionengine.player.setDirection(angle);
            actionengine.ui.reticule.position.X = x - actionengine.ui.reticule.getWidth() / 2;
            actionengine.ui.reticule.position.Y = y - actionengine.ui.reticule.getHeight() / 2;
        } else if (engine.type == Scene.MENU) {
            menuengine.reticule.position.X = x - menuengine.reticule.getWidth() / 2;
            menuengine.reticule.position.Y = y - menuengine.reticule.getHeight() / 2;
            double angle = Services.getAngle(menuengine.options.fighter.position.X + menuengine.options.fighter.getWidth() / 2,
                    menuengine.options.fighter.position.Y + menuengine.options.fighter.getHeight() / 2,
                    x, y);
            menuengine.options.fighter.setDirection(angle);
            String[] keys = menuengine.options.buttons.keySet().toArray(new String[0]);
            for (int i = 0; i < keys.length; i++) {
                Button b = menuengine.options.buttons.get(keys[i]);
                if (x >= b.position.X && x <= b.position.X + b.getWidth()
                        && y >= b.position.Y && y <= b.position.Y + b.getHeight()) {
                    b.unpressed.visible = false;
                    b.depressed.visible = false;
                    b.hoverSelected.visible = true;
                } else {
                    b.unpressed.visible = true;
                    b.depressed.visible = false;
                    b.hoverSelected.visible = false;
                }
            }
        }
    }

    public void handleMouseClicked(MouseEvent e) {
        e.consume();
    }

    public void handleMousePressed(MouseEvent e) {
        oldState = currentState;
        currentState = MOUSE_PRESSED;
        e.consume();
    }

    public void handleMouseReleased(MouseEvent e) {
        oldState = currentState;
        currentState = MOUSE_RELEASED;
        if (engine.type == Scene.ACTION) {
            actionengine.ui.reticule.animation = actionengine.ui.unshot;
        } else if (engine.type == Scene.MENU) {
            menuengine.reticule.animation = menuengine.reticule.animations.get("unshot");
            String[] keys = menuengine.options.buttons.keySet().toArray(new String[0]);
            for (int i = 0; i < keys.length; i++) {
                Button b = menuengine.options.buttons.get(keys[i]);
                b.depressed.visible = false;
                b.hoverSelected.visible = false;
                b.unpressed.visible = true;
            }
        }
        e.consume();
    }

    public void shoot(Vector2 shooterVec, Vector2 mousePos) {
        if (actionengine.player.currentWeapon.bulletsLeft != 0 && actionengine.player.currentWeapon.roundDenom != 0 && actionengine.player.currentWeapon.reloadTimePassed == -1) {
            actionengine.ui.reticule.animation = actionengine.ui.shot;
            actionengine.playerBullets.add(shooterVec, new Vector2(mousePos.X, mousePos.Y), Weapon.accuracy[actionengine.player.currentWeapon.type]);
            actionengine.player.currentWeapon.bulletsLeft--;
            actionengine.ui.weaponBase.ammo.next();
        }
    }

    public void hitButton(Vector2 mousePos) {

        String[] keys = menuengine.options.buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            Button b = menuengine.options.buttons.get(keys[i]);
            if (mousePos.X >= b.position.X && mousePos.X <= b.position.X + b.getWidth()
                    && mousePos.Y >= b.position.Y && mousePos.Y <= b.position.Y + b.getHeight()) {
                b.unpressed.visible = false;
                b.hoverSelected.visible = false;
                b.depressed.visible = true;
                if (b.msg.compareTo("Blue") == 0) {
                    if (menuengine.options.fighter.type == SoldierClass.ENEMY) {
                        menuengine.options.fighter = menuengine.options.blueFighter;
                        menuengine.options.type = SoldierClass.SOLDIER;
                    }
                }
                if (b.msg.compareTo("Yellow") == 0) {
                    if (menuengine.options.fighter.type == SoldierClass.SOLDIER) {
                        menuengine.options.fighter = menuengine.options.yellowFighter;
                        menuengine.options.type = SoldierClass.ENEMY;
                    }
                }
                if (b.msg.compareTo("One Player") == 0) {
                    menuengine.options.numPlayers = 1;
                }
                if (b.msg.compareTo("Two Player") == 0) {
                    menuengine.options.numPlayers = 2;
                }
                if (b.msg.compareTo("Play!") == 0) {
                	Services.deathmatch.switchScenes(Scene.ACTION);
                }
            }


        }

    }
}
