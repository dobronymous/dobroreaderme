/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.reader;

import java.util.Vector;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.attach.AttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.networking.resolve.ResolveDispatcher;
import org.anonymous.dobroreaderme.networking.resolve.ResolveThread;
import org.anonymous.dobroreaderme.ui.ViewablePost;

/**
 *
 * @author sp
 */
public class ThreadReader extends PostsReader implements ResolveDispatcher {

    protected BoardThread thread;
    protected String board;
    protected int id;
    protected Displayable back;

    private class ThreadResolveThread extends ResolveThread {

        protected Api api;
        protected String board;
        protected int id;

        public ThreadResolveThread(Api api, String board, int id) {
            this.api = api;
            this.board = board;
            this.id = id;
        }

        public void action() throws Exception {
            api.loadThread(board, id);
        }
    }

    public ThreadReader(Api api, Midlet midlet, String board, int id) {
        super(api, midlet);
        this.board = board;
        this.id = id;
        this.posts = new Vector();
    }

    public ThreadReader(Api api, Midlet midlet, String board, int id, Displayable back, AttachmentsThumbnailLoader loader) {
        this(api, midlet, board, id);
        this.back = back;
        setAttachmentsThumbnailLoader(loader);
    }

    protected void paint(Graphics g) {
        super.paint(g);
        drawBar(g);
        repaint();
    }

    protected void init() {
        super.init();
        resolve_thread = new ThreadResolveThread(api, board, id);
        resolve_thread.start();
    }

    protected void control(int keyCode, int state) {
        super.control(keyCode, state);
        if (state == 1) {
            if (keyCode == -6) {
                this.thread = null;
                this.posts = null;
                midlet.changeDisplayable(back);
                back = null;
            }
        }
    }

    public void resolved(BoardPost p) {
        posts.addElement(new ViewablePost(p, font, getWidth()));
    }

    public void resolved(BoardThread t) {
        thread = t;
        System.out.println(t);
    }
}
