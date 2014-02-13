/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.ui;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author sp
 */
public class HorizontalLayout extends Layout implements Component {
    public void draw(Graphics g) {
        int offset = 0;
        for (int i = 0; i < getComponents().size(); i++) {
            g.setClip(offset, 0, g.getClipWidth(), g.getClipHeight());
            Component c = (Component) getComponents().elementAt(i);
            c.draw(g);
            offset += c.getWidth();
        }
    }

    public int getWidth() {
        int width = 0;
        for (int i = 0; i < getComponents().size(); i++) {
            Component c = (Component) getComponents().elementAt(i);
            width += c.getWidth();
        }
        
        return width;
    }
    
    public int getHeight() {
        int height = 0;
        for (int i = 0; i < getComponents().size(); i++) {
            Component c = (Component) getComponents().elementAt(i);
            height += c.getHeight();
        }
        
        return height;
    }
}
