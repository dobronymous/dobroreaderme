/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.cache;

import com.decoder.png.PNGEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.lcdui.Image;

/**
 *
 * @author sp
 */
public class FSPNGCache extends FSCache {

    public FSPNGCache(String directory) {
        super(directory);
    }
    protected Image readImage(InputStream is) throws IOException {
        return Image.createImage(is);
    }
    
    protected void writeImage(OutputStream os, Image img) throws IOException {
        byte[][] chans = PNGEncoder.convertIntArrayToByteArrays(img);
        byte[] array = PNGEncoder.toPNG(img.getWidth(), img.getHeight(), chans[0], chans[1], chans[2], chans[3]);
        os.write(array);
    }

    protected String filename(int id) {
        return id+".png";
    }
}