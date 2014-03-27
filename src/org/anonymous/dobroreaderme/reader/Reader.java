/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.reader;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.resolve.ResolveDispatcher;
import org.anonymous.dobroreaderme.networking.resolve.ResolveThread;
import org.anonymous.dobroreaderme.settings.Settings;
import org.anonymous.dobroreaderme.ui.forms.PopupForm;

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
    protected Image[] ticker_image_frames = new Image[11];

    public Reader(Api api, Midlet midlet) {
        this.api = api;
        this.midlet = midlet;

        api.setDispatcher(this);
        setFullScreenMode(true);
    }

    protected void drawBar(Graphics g) {
        g.setColor(Settings.color().bar_background);
        g.fillRoundRect(1, -5, getWidth() - 2, font_height + 5, 4, 4);
        g.setColor(Settings.color().bar_foreground);

        if (getResolveThread() != null && getResolveThread().isAlive()) {

            int x, y, w = font_height / 2 - 1, h = font_height / 2 - 1;
            if (ticker < 2) { // top
                x = getWidth() - 3 - font_height / 2 - ticker * (font_height / 2);
                y = font_height / 2;
            } else { // bottom
                x = getWidth() - 3 - font_height / 2 - Math.abs(ticker - 3) * (font_height / 2);
                y = 1;
            }

            g.setColor(Settings.color().ticker_background);
            g.fillRect(x, y, w, h);

            g.setColor(Settings.color().ticker_outline);
            g.drawRect(x, y, w, h);
            /*
             g.setColor(255, 255, 255);
             g.fillRect(getWidth() - 3 - ticker_image_frames[ticker].getWidth(), 0, ticker_image_frames[ticker].getWidth(), ticker_image_frames[ticker].getHeight()-3);
             g.drawImage(ticker_image_frames[ticker], getWidth()-3, -2, Graphics.TOP | Graphics.RIGHT);
             */

            if (System.currentTimeMillis() - ticker_last > 100) {
                ticker_last = System.currentTimeMillis();
                ticker++;
                if (ticker >= 4) {
                    ticker = 0;
                }
            }
        }
    }

    public ResolveThread getResolveThread() {
        return resolve_thread;
    }

    public void setResolveThread(ResolveThread rt) {
        if (getResolveThread() != null && getResolveThread().isAlive()) {
            throw new IllegalStateException("");
        }

        resolve_thread = rt;
    }

    public void startResolveThread() {
        resolve_thread.start();
    }

    protected void init() {
        for (int i = 0; i <= 10; i++) {
            try {
                ticker_image_frames[i] = Image.createImage(getClass().getResourceAsStream("/res/dobrochan/frame_" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void update() throws Exception {
        if (getResolveThread() != null && getResolveThread().getException() != null) {
            throw getResolveThread().getException();
        }
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

        g.setColor(Settings.color().background);
        g.fillRect(0, 0, getWidth(), getHeight());
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
