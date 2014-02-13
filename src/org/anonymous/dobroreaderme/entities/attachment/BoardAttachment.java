/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.entities.attachment;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.networking.DownloadProgressTracker;

/**
 *
 * @author sp
 */
public class BoardAttachment implements DownloadProgressTracker {
    protected String type;
    protected String src;
    protected String thumb_src;
    protected Image thumbnail;
    protected int size, th, id;
    protected long thumb_size, thumb_downloaded_size;
    protected int loading_state = 0;

    public BoardAttachment(String type, String src, int id, int size, String thumb_src, int th) {
        this.type = type;
        this.src = src;
        this.size = size;
        this.thumb_src = thumb_src;
        this.th = th;
        this.id = id;
    }

    public int getThumbHeight() {
        return th;
    }

    public String getThumbSrc() {
        return thumb_src;
    }

    public Image getThumbnail() {
        return thumbnail;
    }
    
    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
        
        if (thumbnail != null)
            setLoaded();
    }

    public int getId() {
        return id;
    }
    
    public int getLoadingState() {
        return loading_state;
    }
    
    public void setLoaded() {
        this.loading_state = 2;
    }
    
    public void setLoading() {
        this.loading_state = 1;
    }
    
    public void purgeThumbnail() {
        loading_state = 0;
        thumbnail = null;
    }

    public void setTotal(long bytes) {
        thumb_size = bytes;
    }

    public void setCompleted(long bytes) {
        thumb_downloaded_size = bytes;
    }

    public long getCompleted() {
        return thumb_downloaded_size;
    }

    public long getTotal() {
        return thumb_size;
    }
}
