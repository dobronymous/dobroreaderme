/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.resolve;

import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.BoardThread;

/**
 *
 * @author sp
 */
public interface ResolveDispatcher {
    public void resolved(BoardThread t);
    public void resolved(BoardPost p);
}
