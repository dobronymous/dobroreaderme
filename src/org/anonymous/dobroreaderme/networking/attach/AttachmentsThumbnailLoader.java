/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.networking.attach;

import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;
import org.anonymous.dobroreaderme.settings.Settings;

/**
 *
 * @author sp
 */
public class AttachmentsThumbnailLoader extends Thread {

    protected Stack tasks = new Stack();
    protected Vector loaded = new Vector();
    protected boolean locked = false;
    protected Downloader downloader;
    protected Exception exception;

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
                    System.gc();

                    if (Settings.max_mem - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) < Settings.max_mem / 10) {
                        System.out.println(Runtime.getRuntime().freeMemory());
                        BoardAttachment a = (BoardAttachment) loaded.firstElement();
                        a.purgeThumbnail();
                        loaded.removeElementAt(0);
                    }
                }

                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.exception = e;
            } catch (ResolveErrorException e) {
                e.printStackTrace();
                this.exception = e;
            }
        }

    }

    protected void load(BoardAttachment attach) throws ResolveErrorException {
        attach.setThumbnail(downloader.download(attach.getThumbSrc()));
        attach.setLoaded();
    }

    public void free() {
        while (locked) {
        }
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

        if (!loaded.contains(attach)) {
            loaded.addElement(attach);
            tasks.addElement(attach);
        }
    }

    public Exception getException() {
        return exception;
    }

    public void removeException() {
        exception = null;
    }
}
