/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.resolve;

import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.networking.Api;

/**
 *
 * @author sp
 */
public class ThreadedAttachmentLoader extends Thread {
    protected BoardAttachment img;
    protected Api api;

    public ThreadedAttachmentLoader(Api api, BoardAttachment img) {
        this.img = img;
        this.api = api;
    }
    
    public void run() {
        try {
            System.out.println("Loading image " + img.getThumbSrc());
            img.setLoading();
            img.setThumbnail(api.loadImage(img.getThumbSrc()));
            img.setLoaded();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
