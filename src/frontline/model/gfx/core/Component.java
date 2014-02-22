/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package frontline.model.gfx.core;

import java.awt.Graphics2D;

/**
 * TODO: type comment.
 *
 * @version Aug 17, 2011, submitted by Nick Chavez
 */
public interface Component {
  
    
    
    public void draw(Graphics2D g);
    public void update(long timePassed);
    public void loadResources();
}
