/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package frontline.controller;

import frontline.DeathMatch.Scene;
import java.awt.event.KeyEvent;

import frontline.model.ActionScene;
import frontline.model.MenuScene;
import frontline.model.GameScene;
import frontline.model.IntroScene;
import frontline.model.gfx.Fighter;
import frontline.model.gfx.core.tiles.Tile;
import frontline.model.util.Directions;
import frontline.model.util.Services;

import java.util.ArrayList;

/**
 * TODO: type comment.
 * 
 * @version Aug 17, 2011, submitted by Nick Chavez
 */
public class KeyManager {

	public final int OLD_STATE = 0;
	public final int CURRENT_STATE = 1;
	public int offset;
	public GameScene engine;
	public ActionScene actionengine;
	public IntroScene introengine;
	public MenuScene menuengine;
	// In this aray, the index is accessed by the keyCode, and the values it
	// holds are
	// 0.CurrentState
	// 1.OldState
	// True is pressed
	// False is released
	public boolean[][] keyState;

	public KeyManager(GameScene engine) {
		this.engine = engine;
		if (engine.type == Scene.ACTION) {
			actionengine = (ActionScene) engine;
		} else if (engine.type == Scene.INTRO) {
			introengine = (IntroScene) engine;
		} else if (engine.type == Scene.MENU) {
			menuengine = (MenuScene) engine;
		}
		keyState = new boolean[600][2];
		offset = 90;
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

	// What happens when a key is down.
	public void handleKeyPressed(int keyCode) {
		// Update the current and old states.

		keyState[keyCode][OLD_STATE] = keyState[keyCode][CURRENT_STATE];
		keyState[keyCode][CURRENT_STATE] = true;
		if (engine.type == Scene.ACTION) {
			if (!actionengine.announcements.isPaused) {
				if (keyCode == KeyEvent.VK_W) {
					if (canMove(Directions.NORTH, actionengine.player)) {
						if (keyState[KeyEvent.VK_A][CURRENT_STATE]) {
							actionengine.player.move(Directions.NORTHWEST);
						} else if (keyState[KeyEvent.VK_D][CURRENT_STATE]) {
							actionengine.player.move(Directions.NORTHEAST);
						} else {
							actionengine.player.move(Directions.NORTH);
						}
					} else {
						actionengine.player.stop();
					}
				}
				if (keyCode == KeyEvent.VK_A) {
					if (canMove(Directions.WEST, actionengine.player)) {
						if (keyState[KeyEvent.VK_W][CURRENT_STATE]) {
							actionengine.player.move(Directions.NORTHWEST);
						} else if (keyState[KeyEvent.VK_S][CURRENT_STATE]) {
							actionengine.player.move(Directions.SOUTHWEST);
						} else {
							actionengine.player.move(Directions.WEST);

						}
					} else {
						actionengine.player.stop();
					}
				}
				if (keyCode == KeyEvent.VK_S) {
					if (canMove(Directions.SOUTH, actionengine.player)) {
						if (keyState[KeyEvent.VK_A][CURRENT_STATE]) {
							actionengine.player.move(Directions.SOUTHWEST);
						} else if (keyState[KeyEvent.VK_D][CURRENT_STATE]) {
							actionengine.player.move(Directions.SOUTHEAST);
						} else {
							actionengine.player.move(Directions.SOUTH);
						}
					} else {
						actionengine.player.stop();
					}
				}
				if (keyCode == KeyEvent.VK_D) {
					if (canMove(Directions.EAST, actionengine.player)) {
						if (keyState[KeyEvent.VK_W][CURRENT_STATE]) {
							actionengine.player.move(Directions.NORTHEAST);
						} else if (keyState[KeyEvent.VK_S][CURRENT_STATE]) {
							actionengine.player.move(Directions.SOUTHEAST);
						} else {
							actionengine.player.move(Directions.EAST);
						}
					} else {
						actionengine.player.stop();
					}
				}

				if (keyCode == KeyEvent.VK_1) {
					setWeapon(0);
				}

				if (keyCode == KeyEvent.VK_2) {
					setWeapon(1);
				}

				if (keyCode == KeyEvent.VK_3) {
					setWeapon(2);
				}

				if (keyCode == KeyEvent.VK_4) {
					setWeapon(3);
				}

				if (keyCode == KeyEvent.VK_5) {
					setWeapon(4);
				}

				if (keyCode == KeyEvent.VK_R) {
					actionengine.player.currentWeapon.manualReload();
					// update ui
				}
			}

			if (keyCode == KeyEvent.VK_ENTER) {
				actionengine.announcements.intro.active = false;
				actionengine.announcements.isPaused = false;
			}

			if (keyCode == KeyEvent.VK_ESCAPE) {
				Services.deathmatch.stop();
			}
		} else if (engine.type == Scene.INTRO) {
			if (keyCode == KeyEvent.VK_ESCAPE) {
				Services.deathmatch.stop();
			}
		} else if (engine.type == Scene.MENU) {
			if (keyCode == KeyEvent.VK_ESCAPE) {
				Services.deathmatch.stop();
			}
		}
	}

	public void handleKeyReleased(int keyCode) {
		// Update the current and old states.
		keyState[keyCode][OLD_STATE] = keyState[keyCode][CURRENT_STATE];
		keyState[keyCode][CURRENT_STATE] = false;
		if (engine.type == Scene.ACTION) {
			if (keyCode == KeyEvent.VK_W) {
				if (keyState[KeyEvent.VK_A][CURRENT_STATE]) {
					if (canMove(Directions.WEST, actionengine.player)) {
						actionengine.player.move(Directions.WEST);
					}
				} else if (keyState[KeyEvent.VK_D][CURRENT_STATE]) {
					if (canMove(Directions.EAST, actionengine.player)) {
						actionengine.player.move(Directions.EAST);
					}
				} else if (keyState[KeyEvent.VK_S][CURRENT_STATE]) {
					if (canMove(Directions.SOUTH, actionengine.player)) {
						actionengine.player.move(Directions.SOUTH);
					}
				} else {
					actionengine.player.stop();
				}
			}
			if (keyCode == KeyEvent.VK_A) {
				if (keyState[KeyEvent.VK_W][CURRENT_STATE]) {
					if (canMove(Directions.NORTH, actionengine.player)) {
						actionengine.player.move(Directions.NORTH);
					}
				} else if (keyState[KeyEvent.VK_S][CURRENT_STATE]) {
					if (canMove(Directions.SOUTH, actionengine.player)) {
						actionengine.player.move(Directions.SOUTH);
					}
				} else if (keyState[KeyEvent.VK_D][CURRENT_STATE]) {
					if (canMove(Directions.EAST, actionengine.player)) {
						actionengine.player.move(Directions.EAST);
					}
				} else {
					actionengine.player.stop();
				}
			}
			if (keyCode == KeyEvent.VK_S) {
				if (keyState[KeyEvent.VK_W][CURRENT_STATE]) {
					if (canMove(Directions.NORTH, actionengine.player)) {
						actionengine.player.move(Directions.NORTH);
					}
				} else if (keyState[KeyEvent.VK_A][CURRENT_STATE]) {
					if (canMove(Directions.WEST, actionengine.player)) {
						actionengine.player.move(Directions.WEST);
					}
				} else if (keyState[KeyEvent.VK_D][CURRENT_STATE]) {
					if (canMove(Directions.EAST, actionengine.player)) {
						actionengine.player.move(Directions.EAST);
					}
				} else {
					actionengine.player.stop();
				}
			}
			if (keyCode == KeyEvent.VK_D) {
				if (keyState[KeyEvent.VK_W][CURRENT_STATE]) {
					if (canMove(Directions.NORTH, actionengine.player)) {
						actionengine.player.move(Directions.NORTH);
					}
				} else if (keyState[KeyEvent.VK_A][CURRENT_STATE]) {
					if (canMove(Directions.WEST, actionengine.player)) {
						actionengine.player.move(Directions.WEST);
					}
				} else if (keyState[KeyEvent.VK_S][CURRENT_STATE]) {
					if (canMove(Directions.SOUTH, actionengine.player)) {
						actionengine.player.move(Directions.SOUTH);
					}
				} else {
					actionengine.player.stop();
				}
			}
		} else if (engine.type == Scene.INTRO) {

			if (keyCode == KeyEvent.VK_ENTER) {
				try {
					Services.deathmatch.switchScenes(Scene.MENU);
				} catch (Exception e) {

				}
			}
		} else if (engine.type == Scene.MENU) {

			/*
			 * if (keyCode == KeyEvent.VK_SPACE) {
			 * Global.deathmatch.switchScenes(Scene.ACTION); }
			 */

		}
	}

	public void setWeapon(int stockNum) {
		if (actionengine.player.stocks[actionengine.player.type.num].length >= stockNum + 1) {
			actionengine.player.currentWeapon = actionengine.player.stock[stockNum];
			actionengine.player.currentWeapon
					.setDirection(actionengine.player.upperBody.angle);
			actionengine.ui.weaponBase.weapon = actionengine.player.currentWeapon;
			actionengine.ui.weaponBase.ammo = actionengine.ui.weaponBase.sets[actionengine.player.currentWeapon.type];
		}
	}

	public boolean canMove(int direction, Fighter mover) {
		ArrayList<Tile> set;

		if (actionengine.grid != null) {
			set = actionengine.grid.getBlockedTiles();
			for (Tile t : set) {
				int y2 = (int) t.position.Y + t.getHeight();
				int y1 = (int) t.position.Y;
				int x2 = (int) t.position.X + t.getWidth();
				int x1 = (int) t.position.X;
				int buffer = 5;

				if ((((direction >= 13 || direction <= 3)
						&& mover.position.Y <= y2 && (Math.abs(mover.position.Y
						- y2)) < buffer) && ((mover.position.X == x1 && mover.position.X
						+ mover.getWidth() == x2)
						|| (mover.position.X > x1 && mover.position.X < x2)
						|| (mover.position.X + mover.getWidth() > x1 && mover.position.X
								+ mover.getWidth() < x2) || (mover.position.X < x1 && mover.position.X
						+ mover.getWidth() > x2)))
						|| ((direction >= 5 && direction <= 11
								&& mover.position.Y + mover.getHeight() >= y1 && (Math
								.abs(mover.position.Y + mover.getHeight() - y1)) < buffer) && ((mover.position.X == x1 && mover.position.X
								+ mover.getWidth() == x2)
								|| (mover.position.X > x1 && mover.position.X < x2)
								|| (mover.position.X + mover.getWidth() > x1 && mover.position.X
										+ mover.getWidth() < x2) || (mover.position.X < x1 && mover.position.X
								+ mover.getWidth() > x2)))
						|| ((direction >= 1 && direction <= 7
								&& mover.position.X + mover.getWidth() >= x1 && (Math
								.abs(mover.position.X + mover.getWidth() - x1)) < buffer) && ((mover.position.Y == y1 && mover.position.Y
								+ mover.getHeight() == y2)
								|| (mover.position.Y > y1 && mover.position.Y < y2)
								|| (mover.position.Y + mover.getHeight() > y1 && mover.position.Y
										+ mover.getHeight() < y2) || (mover.position.Y < y1 && mover.position.Y
								+ mover.getHeight() > y2)))
						|| ((direction >= 9 && direction <= 15
								&& mover.position.X <= x2 && (Math
								.abs(mover.position.X - x2)) < buffer) && ((mover.position.Y == y1 && mover.position.Y
								+ mover.getHeight() == y2)
								|| (mover.position.Y > y1 && mover.position.Y < y2)
								|| (mover.position.Y + mover.getHeight() > y1 && mover.position.Y
										+ mover.getHeight() < y2) || (mover.position.Y < y1 && mover.position.Y
								+ mover.getHeight() > y2)))) {
					return false;
				}
			}
		}

		return true;
	}
}
