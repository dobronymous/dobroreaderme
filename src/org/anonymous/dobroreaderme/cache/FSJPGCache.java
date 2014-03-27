/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.cache;

import com.decoder.jpg.JPGDecoder;
import com.decoder.jpg.PixelArray;
import com.encoder.jpg.JPGEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.lcdui.Image;

/**
 *
 * @author sp
 */
public class FSJPGCache extends FSCache {

    public FSJPGCache(String directory) {
        super(directory);
    }
    protected Image readImage(InputStream is) throws IOException {
        PixelArray pix = new PixelArray();
        new JPGDecoder().decode(is, pix);

        return Image.createRGBImage(pix.getPix(), pix.getWidth(), pix.getHeight(), false);
    }
    
    protected void writeImage(OutputStream os, Image img) throws IOException {
        os.write(new JPGEncoder().encode(img, 60));
    }

    protected String filename(int id) {
        return id + ".jpg";
    }
}

