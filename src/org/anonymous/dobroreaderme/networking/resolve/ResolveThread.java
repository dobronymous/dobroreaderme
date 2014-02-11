/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.resolve;

/**
 *
 * @author sp
 */
public class ResolveThread extends Thread {
    protected Exception exception;
    
    public void run() {
        try {
            action();
        } catch (Exception e) {
            exception = e;
        }
    }
    
    public void action() throws Exception {
        
    }

    public Exception getException() {
        return exception;
    }
}
