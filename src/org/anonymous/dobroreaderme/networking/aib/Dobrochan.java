/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.aib;

/**
 *
 * @author sp
 */
public class Dobrochan implements AIB {
    protected String host;

    public Dobrochan(String host) {
        this.host = host;
    }
    
    public String getHost() {
        return host;
    }
    
}
