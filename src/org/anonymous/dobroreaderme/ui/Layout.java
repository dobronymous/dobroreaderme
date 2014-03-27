/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.ui;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author sp
 */
public class Layout {
    protected Vector components;
    
    public void addComponent(Component c) {
        components.addElement(c);
    }

    public Vector getComponents() {
        return components;
    }
    
    public void draw(Graphics g) {
        
    }
}
