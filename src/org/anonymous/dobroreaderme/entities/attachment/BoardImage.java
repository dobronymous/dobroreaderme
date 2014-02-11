/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.entities.attachment;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.networking.util.HTTP;

/**
 *
 * @author sp
 */
public class BoardImage extends BoardAttachment {

    protected int w, h;

    public BoardImage(String src, int id, int size, String thumb_src, int w, int h, int th) {
        super("image", src, id, size, thumb_src, th);
        this.w = w;
        this.h = h;
    }

}
