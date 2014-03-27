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
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;

/**
 *
 * @author sp
 */
public abstract class FSCache implements Cache {

    protected String directory;

    public FSCache(String directory) {
        this.directory = directory;

        FileConnection c = null;
        try {
            c = (FileConnection) Connector.open(directory, Connector.READ_WRITE);
            if (!c.exists()) {
                c.create();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean exists(BoardAttachment attach) {
        FileConnection c = null;
        InputStream is = null;
        try {
            c = (FileConnection) Connector.open(directory + filename(attach.getId()), Connector.READ_WRITE);
            return c.exists();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public boolean restore(BoardAttachment attach) {
        FileConnection c = null;
        InputStream is = null;
        try {
            c = (FileConnection) Connector.open(directory + filename(attach.getId()), Connector.READ_WRITE);
            if (c.exists()) {
                is = c.openInputStream();

                attach.setThumbnail(readImage(is));
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public void store(BoardAttachment attach) {
        FileConnection c = null;
        OutputStream os = null;
        try {
            c = (FileConnection) Connector.open(directory + filename(attach.getId()), Connector.READ_WRITE);
            if (c.exists()) {
                c.delete();
            }

            c.create();
            os = c.openOutputStream();

            writeImage(os, attach.getThumbnail());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    protected abstract Image readImage(InputStream is) throws IOException;

    protected abstract void writeImage(OutputStream os, Image img) throws IOException;

    protected abstract String filename(int id);

}
