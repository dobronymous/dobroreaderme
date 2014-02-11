/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.attach;

import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;

/**
 *
 * @author sp
 */
public class FSCache implements Cache {
    protected String directory;

    public FSCache(String directory) {
        this.directory = directory;
    }
    
    public boolean resotre(BoardAttachment attach) {
        return false;
    }
}
