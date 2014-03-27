/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.ui.menu;

import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.networking.attach.AttachmentsThumbnailLoader;

/**
 *
 * @author sp
 */
public class AttachmentItem extends Item {
    protected BoardAttachment attach;
    protected AttachmentsThumbnailLoader loader;

    public AttachmentItem(int x, int y, BoardAttachment attach, AttachmentsThumbnailLoader loader) {
        super(x, y, -1, -1);
        this.attach = attach;
        this.loader = loader;
    }

    public int getH() {
        return attach.getThumbHeight();
    }

    public int getW() {
        return attach.getThumbnail() == null ? attach.getThumbHeight() : attach.getThumbnail().getWidth();
    }
    
    public void pull() {
        if (attach.getThumbnail() == null)
            loader.addTask(attach);
    }

}
