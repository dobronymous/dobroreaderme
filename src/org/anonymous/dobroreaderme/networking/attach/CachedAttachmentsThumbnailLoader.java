/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.attach;

import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;

/**
 *
 * @author sp
 */
public class CachedAttachmentsThumbnailLoader extends AttachmentsThumbnailLoader {
    protected Cache cache;
    
    public CachedAttachmentsThumbnailLoader(Downloader downloader, Cache cache) {
        super(downloader);
        this.cache = cache;
    }

    protected void load(BoardAttachment attach) throws ResolveErrorException {
        if (!cache.resotre(attach)) {
            super.load(attach);
        }
    }
}
