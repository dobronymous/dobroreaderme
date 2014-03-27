/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.ui.menu;

import java.util.Vector;

/**
 *
 * @author sp
 */
public class Menu {
    protected Vector items = new Vector();
    
    public void addItem(Item i) {
        items.addElement(i);
    }
    
    public void clearItems() {
        items.removeAllElements();
    }
    
    public Item getCursorFor(int x, int y) {
        if (items.isEmpty())
            return null;
        
        int[] distances = new int[items.size()];
        double min = -1;
        int index = -1;
        
        for (int i = 0; i < items.size(); i++) {
            Item item = (Item) items.elementAt(i);
            int a = x - item.getX();
            int b = y - item.getY();
            double distance = Math.sqrt(a*a + b*b);
            if (min == -1 || distance < min) {
                min = distance;
                index = i;
            }
        }
        
        return (Item) items.elementAt(index);
    }
}
