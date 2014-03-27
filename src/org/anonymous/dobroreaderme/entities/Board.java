/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.entities;

import java.util.Vector;

/**
 *
 * @author sp
 */
public class Board {
    protected Vector threads;
    
    public Board(Vector threads) {
        this.threads = threads;
    }
    
    public Vector getThreads() {
        return threads;
    }
}
