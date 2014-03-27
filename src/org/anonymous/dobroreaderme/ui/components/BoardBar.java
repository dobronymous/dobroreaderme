/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.ui.components;

import javax.microedition.lcdui.Graphics;
import org.anonymous.dobroreaderme.ui.Component;
import org.anonymous.dobroreaderme.ui.HorizontalLayout;

/**
 *
 * @author sp
 */
public class BoardBar extends HorizontalLayout implements Component {
    protected int width, height;
    
    public void draw(Graphics g) {
        
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
