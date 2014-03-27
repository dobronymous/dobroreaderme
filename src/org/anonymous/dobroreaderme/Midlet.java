/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStoreException;
import org.anonymous.dobroreaderme.networking.aib.Dobrochan;
import org.anonymous.dobroreaderme.networking.dobrochan.DobrochanApi;
import org.anonymous.dobroreaderme.reader.BoardReader;
import org.anonymous.dobroreaderme.settings.Settings;

/**
 * @author sp
 */
public class Midlet extends MIDlet {
    public void startApp() {
        /*
        try {
            Settings.restore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        
        try {
            Settings.store();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        */
        
        if (Settings.max_mem == -1) {
            Settings.max_mem = (int) getMaxMem();
        }

        try {
            changeDisplayable(new BoardReader(
                    new DobrochanApi(new Dobrochan("http://dobrochan.com")),
                    this,
                    "b",
                    0
            ));
        } catch (Exception e) {
            e.printStackTrace();
            Form f = new Form("123");
            f.append(e.getMessage());
            changeDisplayable(f);
        }
    }

    public void changeDisplayable(Displayable c) {
        Display.getDisplay(this).setCurrent(c);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    
    protected long getMaxMem() {
        long max_mem = 0;
        try {
            Vector strings = new Vector();
            while (true) {
                strings.addElement(new String("stress test; stress test; stress test;"));

                if (Runtime.getRuntime().totalMemory() > Settings.max_mem) {
                    max_mem = Runtime.getRuntime().totalMemory();
                }
            }
        } catch (OutOfMemoryError e) {
        }
        System.gc();
        
        return max_mem;
    }

}
