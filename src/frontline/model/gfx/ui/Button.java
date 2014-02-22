/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontline.model.gfx.ui;

import frontline.model.gfx.core.Component;
import frontline.model.gfx.core.Sprite;
import frontline.model.util.Services;
import frontline.model.util.Vector2;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/** Copyright 2011, Bleep Bloop Software.
 *  Unpublished Copyright, All Rights Reserved
 *
 */
/**
 *
 * @author Nick
 */
public class Button implements Component {

    public Vector2 position;
    public boolean hud;
    public Sprite unpressed;
    public Sprite depressed;
    public Sprite hoverSelected;
    public String msg;

    public Button(Image sheet, String msg, boolean hud) {
        this.hud = hud;
        this.position = new Vector2();
        this.msg = msg;
        loadResources(sheet);
    }

    public Button(Image sheet, String msg, boolean hud, Vector2 position) {
        this.hud = hud;
        this.position = position;
        this.msg = msg;
        loadResources(sheet);
    }

    @Override
    public void draw(Graphics2D g) {
        unpressed.draw(g);
        depressed.draw(g);
        hoverSelected.draw(g);
        drawMsg(g);
    }

    public void drawMsg(Graphics2D g) {
        
        g.setColor(new Color(100, 100, 100));
        FontMetrics fm = g.getFontMetrics(g.getFont());
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(msg, g);
        int x = (int) (position.X + ((getWidth()) - rect.getWidth()) / 2);
        int y = (int) (position.Y + ((getHeight()) - rect.getHeight()) / 2) + 15;
        g.drawString(msg, x, y);
    }

    @Override
    public void update(long timePassed) {
        unpressed.update(timePassed);
        depressed.update(timePassed);
        hoverSelected.update(timePassed);
    }

    public void loadResources() {
    }

    public void loadResources(Image sheet) {
        Image u = Services.crop(Services.toBufferedImage(sheet), new Rectangle(0, 0, sheet.getWidth(null) / 3, sheet.getHeight(null)));
        Image d = Services.crop(Services.toBufferedImage(sheet), new Rectangle(sheet.getWidth(null) / 3, 0, sheet.getWidth(null) / 3, sheet.getHeight(null)));
        Image s = Services.crop(Services.toBufferedImage(sheet), new Rectangle(2 * (sheet.getWidth(null) / 3), 0, sheet.getWidth(null) / 3, sheet.getHeight(null)));
        unpressed = new Sprite(Services.makeColorTransparent(u, Color.BLACK));
        depressed = new Sprite(Services.makeColorTransparent(d, Color.BLACK));
        hoverSelected = new Sprite(Services.makeColorTransparent(s, Color.BLACK));
        unpressed.position = position;
        depressed.position = position;
        hoverSelected.position = position;
        depressed.visible = false;
        hoverSelected.visible = false;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        unpressed.position = position;
        depressed.position = position;
        hoverSelected.position = position;
    }

    public int getWidth() {
        return unpressed.getWidth();
    }

    public int getHeight() {
        return unpressed.getHeight();
    }
}
