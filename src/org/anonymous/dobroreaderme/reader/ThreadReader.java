/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.reader;

import java.util.Vector;
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
    protected Reader back;

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
    
    private class ThreadUpdateThread extends ResolveThread {
        protected Api api;
        protected String board;
        protected BoardThread thread;

        public ThreadUpdateThread(Api api, String board, BoardThread thread) {
            this.api = api;
            this.board = board;
            this.thread = thread;
        }

        public void action() throws Exception {
            api.updateThread(board, thread);
        } 
    }
    
    public ThreadReader(Api api, Midlet midlet, String board, int id) {
        super(api, midlet);
        this.board = board;
        this.id = id;
        this.posts = new Vector();
    }

    public ThreadReader(Api api, Midlet midlet, String board, int id, Reader back, AttachmentsThumbnailLoader loader) {
        this(api, midlet, board, id);
        this.back = back;
        setAttachmentsThumbnailLoader(loader);
    }

    protected void paint(Graphics g) {
        super.paint(g);
        drawBar(g);
        repaint();
    }

    protected void drawBar(Graphics g) {
        super.drawBar(g);

        if (thread != null) {
            String thread_str = ">>" + board + "/" + id;
            int posts_count = thread.getPostsCount();

            String post_str = (posts.size()) + "/" + posts_count;
            int thread_str_len = font.stringWidth(post_str);
            int percent = (int) Math.ceil((posts.size() * thread_str_len + 1) / (posts_count));

            int offset = 0;
            g.drawString(thread_str + "", 0, 0, 0);
            offset += font.stringWidth(thread_str) + 10;

            g.setColor(0, 0, 0);
            g.fillRect(offset - 3, 0, thread_str_len + 6, font_height);
            g.setColor(100, 100, 100);
            g.fillRect(offset - 3, 0, percent + 6, font_height);
            g.setColor(92, 0, 0);
            g.drawString(post_str, offset, 0, 0);
        }
    }

    protected void init() {
        super.init();
        
        setResolveThread(new ThreadResolveThread(api, board, id));
        startResolveThread();
    }

    protected void control(int keyCode, int state) {
        super.control(keyCode, state);
        if (state == 1) {
            if (keyCode == -6) {
                this.thread = null;
                this.posts = null;
                api.setDispatcher(back); //@TODO
                midlet.changeDisplayable(back);
                back = null;
            }
            
            if (keyCode == 35) {
                setResolveThread(new ThreadUpdateThread(api, board, thread));
                startResolveThread();
            }
        }
    }

    public void resolved(BoardPost p) {
        posts.addElement(new ViewablePost(p, font, getWidth() - 6));
        thread.getPosts().removeAllElements();
        thread.getPosts().addElement(p);
    }

    public void resolved(BoardThread t) {
        thread = t;
    }
}
