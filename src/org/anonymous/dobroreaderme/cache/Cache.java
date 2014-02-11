/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.cache;

import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;

/**
 *
 * @author sp
 */
public interface Cache {
    public boolean restore(BoardAttachment attach);
    public void store(BoardAttachment attach);
}
