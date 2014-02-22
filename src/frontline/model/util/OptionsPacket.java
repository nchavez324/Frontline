/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontline.model.util;

import frontline.model.gfx.Fighter.SoldierClass;

/** Copyright 2011, Bleep Bloop Software.
 *  Unpublished Copyright, All Rights Reserved
 *
 */
/**
 *
 * @author Nick
 */
public class OptionsPacket {
    
    public SoldierClass type;
    public int numPlayers;
    
    public OptionsPacket(SoldierClass type, int numPlayers){
        this.type = type;
        this.numPlayers = numPlayers;
    }
    
}
