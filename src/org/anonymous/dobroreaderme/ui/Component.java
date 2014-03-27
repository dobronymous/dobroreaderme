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
public interface Component {
    public void draw(Graphics g);
    public int getWidth();
    public int getHeight();
}
