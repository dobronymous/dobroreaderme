/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.networking.resolve.ResolveDispatcher;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;

/**
 *
 * @author sp
 */
public interface Api {
    public void setDispatcher(ResolveDispatcher d);
    public Image loadImage(String src, DownloadProgressTracker track) throws ResolveErrorException;
    public String imagePath(String src) throws ResolveErrorException;
    
    public void reply(String board, BoardThread thread, String name, String message, String subject, String captcha);
    
    public void updateThread(String board, BoardThread thread) throws ResolveErrorException;
    public void loadThread(String board, int id) throws ResolveErrorException;
    public void loadBoard(String board, int page) throws ResolveErrorException;
}
