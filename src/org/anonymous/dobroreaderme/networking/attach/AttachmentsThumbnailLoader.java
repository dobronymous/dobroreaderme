/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.networking.attach;

import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;

/**
 *
 * @author sp
 */
public class AttachmentsThumbnailLoader extends Thread {
    protected Stack tasks = new Stack();
    protected Vector loaded = new Vector();
    protected boolean locked = false;
    protected Downloader downloader;

    public AttachmentsThumbnailLoader(Downloader downloader) {
        this.downloader = downloader;
    }

    public void run() {
        while (true) {
            try {
                if (!tasks.isEmpty()) {
                    locked = true;
                    BoardAttachment task = (BoardAttachment) tasks.pop();
                    locked = false;

                    load(task);

                    if (Runtime.getRuntime().freeMemory() < Runtime.getRuntime().totalMemory()/4) {
                        BoardAttachment a = (BoardAttachment) loaded.firstElement();
                        a.purgeThumbnail();
                        loaded.removeElementAt(0);
                    }
                }
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ResolveErrorException e) {
                e.printStackTrace();
            }
        }
    }

    protected void load(BoardAttachment attach) throws ResolveErrorException {
        attach.setThumbnail(downloader.download(attach.getThumbSrc()));
        attach.setLoaded();
    }

    public void free() {
        while (locked) {}
        locked = true;
        for (int i = 0; i < loaded.size(); i++) {
            BoardAttachment a = (BoardAttachment) loaded.elementAt(i);
            a.purgeThumbnail();
        }
        loaded.removeAllElements();
        tasks.removeAllElements();
        locked = false;        
    }
    
    public void addTask(BoardAttachment attach) {
        attach.setLoading();
        while (locked) {}
        tasks.addElement(attach);
        loaded.addElement(attach);
    }

}
