/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontline.model.gfx.ui;

import frontline.model.MenuScene;
import frontline.model.gfx.Fighter;
import frontline.model.gfx.Fighter.SoldierClass;
import frontline.model.gfx.core.Component;
import frontline.model.util.OptionsPacket;
import frontline.model.util.Vector2;
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
public class OptionsUI implements Component {

    public HashMap<String, Button> buttons;
    public Fighter blueFighter;
    public Fighter yellowFighter;
    public Fighter fighter;
    //Options
    public SoldierClass type;
    public int numPlayers;

    public OptionsUI() {
        loadResources();

    }

    @Override
    public void draw(Graphics2D g) {
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            buttons.get(keys[i]).draw(g);
        }
        fighter.draw(g);
        /*Font f = g.getFont();
        g.setFont(MenuScene.depot.fonts.get("WideAwakeSolid").deriveFont(Font.PLAIN, 70));
        g.setColor(Color.white);
        g.drawString("" + numPlayers, 510, 350);
        g.setFont(MenuScene.depot.fonts.get("WideAwakeSolid").deriveFont(Font.PLAIN, 30));
        g.drawString("No options for now!", 145, 330);
        g.setFont(f);*/
        
    }

    @Override
    public void update(long timePassed) {
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            buttons.get(keys[i]).update(timePassed);
        }
        fighter.update(timePassed);
    }

    @Override
    public void loadResources() {
        Button blue = new Button(MenuScene.depot.ui.get("button"), "Blue", true);
        blue.setPosition(new Vector2(50, 200));
        Button yellow = new Button(MenuScene.depot.ui.get("button"), "Yellow", true);
        yellow.setPosition(new Vector2(300, 200));
        Button play = new Button(MenuScene.depot.ui.get("button"), "Play!", true);
        play.setPosition(new Vector2(610, 535));
        Button onePlayer = new Button(MenuScene.depot.ui.get("button"), "One Player", true);
        onePlayer.setPosition(new Vector2(50, 300));
        Button twoPlayer = new Button(MenuScene.depot.ui.get("button"), "Two Player", true);
        twoPlayer.setPosition(new Vector2(300, 300));
        numPlayers = 1;
        
        buttons = new HashMap<String, Button>();
        buttons.put("Blue", blue);
        buttons.put("Yellow", yellow);
        buttons.put("Play", play);
        //buttons.put("One Player", onePlayer);
        //buttons.put("Two Player", twoPlayer);

        blueFighter = new Fighter(SoldierClass.SOLDIER);
        blueFighter.position.X = 500;
        blueFighter.position.Y = 195;
        blueFighter.setDirection(270);

        yellowFighter = new Fighter(SoldierClass.ENEMY);
        yellowFighter.position.X = 500;
        yellowFighter.position.Y = 195;
        yellowFighter.setDirection(270);

        fighter = blueFighter;
        type = SoldierClass.SOLDIER;
        
    }

    public OptionsPacket getOptions() {
        return new OptionsPacket(type, numPlayers);
    }
}
