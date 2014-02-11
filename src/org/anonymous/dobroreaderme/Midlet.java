/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme;

import org.anonymous.dobroreaderme.reader.BoardReader;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.*;
import org.anonymous.dobroreaderme.networking.aib.Dobrochan;
import org.anonymous.dobroreaderme.networking.attach.AttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.networking.dobrochan.DobrochanApi;
import org.anonymous.dobroreaderme.reader.ThreadReader;

/**
 * @author sp
 */
public class Midlet extends MIDlet {
   
    public void startApp() {
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
    
    public void pauseApp() {}
    public void destroyApp(boolean unconditional) {}

}
