/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package frontline.model;

import Resources.ResourceLoader;
import frontline.DeathMatch.Scene;
import frontline.model.gfx.AIFighter;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;


import frontline.model.gfx.Background;
import frontline.model.gfx.Bullet;
import frontline.model.gfx.Bullets;
import frontline.model.gfx.Fighter;
import frontline.model.gfx.Weapon;
import frontline.model.gfx.Fighter.SoldierClass;
import frontline.model.gfx.core.Announcements;
import frontline.model.gfx.core.Component;
import frontline.model.gfx.core.tiles.Map;
import frontline.model.gfx.ui.UserInterface;
import frontline.model.util.OptionsPacket;
import frontline.model.util.Services;
import frontline.model.util.Vector2;

/**
 * TODO: type comment.
 *
 * @version Aug 17, 2011, submitted by Nick Chavez
 */
public class ActionScene extends GameScene {

    public static final int BACKGROUND = 0;
    public static final int TILEGRID = 1;
    public static final int PLAYER = 2;
    public static final int ENEMY = 3;
    public static final int PLAYERBULLETS = 4;
    public static final int ENEMYBULLETS = 5;
    public static final int BLOODS = 6;
    public static final int USERINTERFACE = 7;
    public static final int ANNOUNCEMENTS = 8;
    public static final int PROMPT = 9;
    public HashMap<Integer, Component> components;
    public Fighter player;
    public Fighter enemy;
    public UserInterface ui;
    public Bullets playerBullets;
    public Bullets enemyBullets;
    public Announcements announcements;
    public Map grid;
    public static ResourceLoader depot;

    public ActionScene(String where, SoldierClass playerType) throws IOException {
        /*try {
        System.setOut(new PrintStream("C:\\mylog.txt\\"));
        } catch (FileNotFoundException ex) {
        Logger.getLogger(ActionScene.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        type = Scene.ACTION;
        components = new HashMap<Integer, Component>();
        initComponents(where, playerType);
    }

    public void update(long timePassed) {
        if (running) {
            if (!announcements.isPaused) {
                Integer[] keys = components.keySet().toArray(new Integer[0]);
                for (int i = 0; i < keys.length; i++) {
                    components.get(keys[i]).update(timePassed);
                }
                checkBulletCollisions();
            }
            updateCamera();
        }
    }

    public void initComponents(String where, SoldierClass playerType) throws IOException {
        depot = new ResourceLoader();
        
        SoldierClass enemyType = SoldierClass.ENEMY;
        if(playerType == SoldierClass.SOLDIER){enemyType = SoldierClass.ENEMY;}
        else if(playerType == SoldierClass.ENEMY){enemyType = SoldierClass.SOLDIER;}
        
        components.put(BACKGROUND, new Background("bg"));
        components.put(TILEGRID, (depot.process(depot.mapInputs.get("MapOne"), depot.tilesets.get("overworld"))));
        components.put(PLAYER, new Fighter(playerType));
        components.put(USERINTERFACE, new UserInterface(((Fighter) components.get(PLAYER)), (Fighter) components.get(ENEMY)));
        components.put(PLAYERBULLETS, new Bullets());
        components.put(ENEMYBULLETS, new Bullets());
        components.put(ENEMY, new AIFighter(enemyType));
        components.put(ANNOUNCEMENTS, new Announcements());

        player = (Fighter) components.get(PLAYER);
        enemy = (Fighter) components.get(ENEMY);
        ui = (UserInterface) components.get(USERINTERFACE);
        playerBullets = (Bullets) components.get(PLAYERBULLETS);
        announcements = (Announcements) components.get(ANNOUNCEMENTS);
        enemyBullets = (Bullets) components.get(ENEMYBULLETS);
        grid = (Map) components.get(TILEGRID);
        Services.grid = this.grid;
        Services.enemyBullets = enemyBullets;

        enemy.position = new Vector2(400, 250);
        enemy.setDirection(180);

        player.position = new Vector2(200, 350);

        ui.otherMeter.enemy = enemy;

        ((Announcements) components.get(ANNOUNCEMENTS)).enemyMeter = ui.otherMeter;
        ((Announcements) components.get(ANNOUNCEMENTS)).playerMeter = ui.health;
        Services.camera = new Rectangle(0, 0, Services.screenWidth, Services.screenHeight);
    }

    public void moveFighter(Fighter fighter, int direction) {
        if (fighter.toHitBounds(direction) || toHitBlocked(fighter)) {
            stopFighter(fighter);
            fighter.legs.setDirection(direction);
        } else {
            fighter.legs.setDirection(direction);
            fighter.moving = true;
            fighter.setVelocity(direction);
        }
    }

    public void stopFighter(Fighter fighter) {
        fighter.velocity.X = 0f;
        fighter.velocity.Y = 0f;
        fighter.moving = false;
    }

    public void draw(Graphics2D g) {
        if (running) {
            Integer[] keys = components.keySet().toArray(new Integer[0]);
            for (int i = 0; i < keys.length; i++) {
                components.get(keys[i]).draw(g);
            }
        }
    }

    public synchronized void checkBulletCollisions() {
        for (Iterator<Bullet> itr = playerBullets.liveBullets.iterator(); itr.hasNext();) {
            Bullet bill = itr.next();
            Rectangle billRect = new Rectangle((int) bill.position.X, (int) bill.position.Y, bill.getWidth(), bill.getHeight());
            Rectangle enemyRect = new Rectangle((int) enemy.position.X, (int) enemy.position.Y,
                    enemy.getWidth(), enemy.getHeight());

            Rectangle tileRect = grid.getRect(bill.position);
            if (grid.isBlocked(bill.position) && tileRect.intersectsLine(bill.oldPos.X, bill.oldPos.Y, bill.position.X, bill.position.Y)) {
                itr.remove();
            } else if (billRect.intersects(enemyRect)
                    || enemyRect.intersectsLine(bill.oldPos.X, bill.oldPos.Y, bill.position.X, bill.position.Y)) {
                ui.otherMeter.percentage -= Weapon.accuracy[player.currentWeapon.type];
                itr.remove();
            }


        }

        for (Iterator<Bullet> itr = enemyBullets.liveBullets.iterator(); itr.hasNext();) {
            Bullet bill = itr.next();
            Rectangle billRect = new Rectangle((int) bill.position.X, (int) bill.position.Y, bill.getWidth(), bill.getHeight());
            Rectangle playerRect = new Rectangle((int) player.position.X, (int) player.position.Y,
                    player.getWidth(), player.getHeight());
            Rectangle tileRect = grid.getRect(bill.position);
            if (grid.isBlocked(bill.position) && tileRect.intersectsLine(bill.oldPos.X, bill.oldPos.Y, bill.position.X, bill.position.Y)) {
                itr.remove();
            } else if (billRect.intersects(playerRect)
                    || playerRect.intersectsLine(bill.oldPos.X, bill.oldPos.Y, bill.position.X, bill.position.Y)) {
                ui.health.percentage -= Weapon.accuracy[enemy.currentWeapon.type];
                ui.blood.visible = true;
                itr.remove();
            }
        }

        Services.playerPos = player.position;
    }

    public boolean toHitBlocked(Fighter fighter) {
        boolean ans = false;
        return ans;
    }

    public void updateCamera() {

        Fighter player = (Fighter) components.get(PLAYER);
        int cenx = (int) player.position.X + (player.getWidth() / 2);
        int ceny = (int) player.position.Y + (player.getHeight() / 2);
        Services.camera.x = cenx - (Services.camera.width / 2);
        Services.camera.y = ceny - (Services.camera.height / 2);

    }
    
    public void setOptions(OptionsPacket options){
        
        SoldierClass playerType = options.type;
        SoldierClass enemyType = SoldierClass.ENEMY;
        if(playerType == SoldierClass.SOLDIER){enemyType = SoldierClass.ENEMY;}
        else if(playerType == SoldierClass.ENEMY){enemyType = SoldierClass.SOLDIER;}
        
        components.put(PLAYER, new Fighter(playerType));
        components.put(ENEMY, new AIFighter(enemyType));
        components.put(USERINTERFACE, new UserInterface(((Fighter) components.get(PLAYER)), (Fighter) components.get(ENEMY)));
        
        player = (Fighter) components.get(PLAYER);
        enemy = (Fighter) components.get(ENEMY);
        ui = (UserInterface) components.get(USERINTERFACE);
        
        player.position = new Vector2(200, 350);
        
        enemy.position = new Vector2(400, 250);
        enemy.setDirection(180);
        ui.otherMeter.enemy = enemy;
        
    }
    
}
