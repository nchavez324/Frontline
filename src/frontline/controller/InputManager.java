/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package frontline.controller;

import frontline.DeathMatch.Scene;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import frontline.model.ActionScene;
import frontline.model.MenuScene;
import frontline.model.GameScene;
import frontline.model.IntroScene;

/**
 * TODO: type comment.
 *
 * @version Aug 17, 2011, submitted by Nick Chavez
 */
public class InputManager implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener {

    private Window window;
    public GameScene engine;
    public ActionScene actionengine;
    public IntroScene introengine;
    public MenuScene menuengine;
    public boolean setListeners;
    private KeyManager keyManager;
    public MouseManager mouseManager;

    public InputManager(GameScene engine, Window window) {
        this.engine = engine;
        if (engine.type == Scene.ACTION) {
            actionengine = (ActionScene) engine;
        } else if (engine.type == Scene.INTRO) {
            introengine = (IntroScene) engine;
        } else if (engine.type == Scene.MENU) {
            menuengine = (MenuScene) engine;
        }
        keyManager = new KeyManager(engine);
        mouseManager = new MouseManager(engine);
        this.window = window;
        setListeners();
        setListeners = false;
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
        keyManager.switchScenes(engine);
        mouseManager.switchScenes(engine);
    }

    //If a key is pressed, it depends on what scene is open,
    //and goes to that method.  
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyManager.handleKeyPressed(keyCode);
        e.consume();
    }

    //If a key is released, it depends on what scene is open,
    //and goes to that method.  
    public void keyReleased(KeyEvent e) {
        if (engine.type == Scene.ACTION) {
            if (!actionengine.announcements.isPaused) {
                int keyCode = e.getKeyCode();
                keyManager.handleKeyReleased(keyCode);
            }
        } else if (engine.type == Scene.INTRO) {
            int keyCode = e.getKeyCode();
            keyManager.handleKeyReleased(keyCode);
        } else if (engine.type == Scene.MENU) {
            int keyCode = e.getKeyCode();
            keyManager.handleKeyReleased(keyCode);
        }
        e.consume();
    }

    //I think that I explained that you would only use this if you were
    //Making the next Microsoft Word. Which you're not. I think.
    //Btw, e.consume() and <whatever>.dispose basically releases
    //The stuff from memory. Since we don't need the KeyEvent after 
    //We've processed it, we can throw this out.
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    public void mouseDragged(MouseEvent e) {
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
        if (engine.type == Scene.ACTION) {
            if (!actionengine.announcements.isPaused) {
                mouseManager.handleMouseMoved(e.getX(), e.getY());
            }
        } else if (engine.type == Scene.MENU) {
            mouseManager.handleMouseMoved(e.getX(), e.getY());
        }
        e.consume();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        e.consume();
    }

    public void mouseClicked(MouseEvent e) {
        if (engine.type == Scene.ACTION) {
            if (!actionengine.announcements.isPaused) {
                mouseManager.handleMouseClicked(e);
            } else if (engine.type == Scene.MENU) {
                mouseManager.handleMouseClicked(e);
            }
        }
        e.consume();
    }

    public void mousePressed(MouseEvent e) {
        if (engine.type == Scene.ACTION) {
            if (!actionengine.announcements.isPaused) {
                mouseManager.handleMousePressed(e);
            }
        } else if (engine.type == Scene.MENU) {
            mouseManager.handleMousePressed(e);
        }
        e.consume();
    }

    public void mouseReleased(MouseEvent e) {
        if (engine.type == Scene.ACTION) {
            if (!actionengine.announcements.isPaused) {
                mouseManager.handleMouseReleased(e);
            }
        } else if (engine.type == Scene.MENU) {
            mouseManager.handleMouseReleased(e);
        }
        e.consume();
    }

    public void mouseEntered(MouseEvent e) {
        e.consume();
    }

    public void mouseExited(MouseEvent e) {
        e.consume();
    }
    //Basically sets up for hearing.

    public void setListeners() {
        this.window.setFocusTraversalKeysEnabled(false); // Makes the Tab button meaningless.
        this.window.addKeyListener(this); //Makes this a key listener. If you erase this line, input would die!!
        this.window.addMouseListener(this);
        this.window.addMouseMotionListener(this);
        this.window.addMouseWheelListener(this);
        setListeners = true;
    }
}
