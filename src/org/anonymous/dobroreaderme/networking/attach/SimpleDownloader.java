/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.attach;

import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.DownloadProgressTracker;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;

/**
 *
 * @author sp
 */
public class SimpleDownloader implements Downloader {
    protected Api api;

    public SimpleDownloader(Api api) {
        this.api = api;
    }
    
    public Image download(String source, DownloadProgressTracker tracker) throws ResolveErrorException {
        return api.loadImage(source, tracker);
    }
    
}
