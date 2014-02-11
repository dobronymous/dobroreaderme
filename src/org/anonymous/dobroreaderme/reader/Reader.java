/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.reader;

import java.io.PrintStream;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.resolve.ResolveDispatcher;
import org.anonymous.dobroreaderme.networking.resolve.ResolveThread;
import org.anonymous.dobroreaderme.ui.PopupForm;
import org.json.me.StringWriter;

/**
 *
 * @author sp
 */
public class Reader extends Canvas implements ResolveDispatcher {
    protected Api api;
    protected Midlet midlet;
    protected ResolveThread resolve_thread;

    // init method
    protected boolean init = false;
    protected int font_height = 0;
    protected Font font;

    // loading ticket
    protected int ticker = 0;
    protected long ticker_last = 0;

    public Reader(Api api, Midlet midlet) {
        this.api = api;
        this.midlet = midlet;

        api.setDispatcher(this);
        setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        if (!init) {
            font = g.getFont();
            font_height = font.getHeight();
            init = true;
            init();
        }

        try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
            
            resolve_thread = null;
            midlet.changeDisplayable(new PopupForm(new String[]{e.getClass().toString(), e.getMessage()}, midlet, this));
        }

        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    protected void drawBar(Graphics g) {
        g.setColor(0, 0, 50);
        g.fillRect(0, 0, getWidth(), font_height);
        g.setColor(255, 255, 255);

        System.gc();
        g.drawString("" + (Runtime.getRuntime().freeMemory()/1024), getWidth()-50, 0, 0);
        if (resolve_thread != null && resolve_thread.isAlive()) {
            if (ticker < 2) {
                g.fillRect(getWidth() - font_height / 2 - ticker * (font_height / 2), font_height / 2, font_height / 2, font_height / 2);
            } else {
                g.fillRect(getWidth() - font_height / 2 - Math.abs(ticker - 3) * (font_height / 2), 0, font_height / 2, font_height / 2);
            }

            if (System.currentTimeMillis() - ticker_last > 100) {
                ticker_last = System.currentTimeMillis();
                ticker++;
                if (ticker > 3) {
                    ticker = 0;
                }
            }
        }
    }

    protected void init() {
    }

    protected void update() throws Exception {
        if (resolve_thread != null && resolve_thread.getException() != null) {
            throw resolve_thread.getException();
        }
    }

    protected void control(int keyCode, int state) {
    }

    protected void keyRepeated(int keyCode) {
        control(keyCode, 2);
    }

    protected void keyReleased(int keyCode) {
        control(keyCode, 3);
    }

    protected void keyPressed(int keyCode) {
        control(keyCode, 1);
    }

    public void resolved(BoardThread t) {
    }

    public void resolved(BoardPost p) {
    }
}
