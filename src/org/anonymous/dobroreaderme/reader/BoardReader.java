/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.reader;

import org.anonymous.dobroreaderme.ui.ChangeBoardForm;
import java.util.Vector;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.attach.AttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.networking.attach.CachedAttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.networking.attach.FSCache;
import org.anonymous.dobroreaderme.networking.attach.SimpleDownloader;
import org.anonymous.dobroreaderme.networking.resolve.ResolveThread;
import org.anonymous.dobroreaderme.ui.ViewablePost;

/**
 *
 * @author sp
 */
public class BoardReader extends PostsReader {
    protected String board;
    protected int page;
    protected int thread_offset;
    protected Vector threads_posts = new Vector();
    protected Vector threads = new Vector();

    private class BoardResolveThread extends ResolveThread {
        protected Api api;
        protected String board;
        protected int page;

        public BoardResolveThread(Api api, String board, int page) {
            this.api = api;
            this.board = board;
            this.page = page;
        }

        public void action() throws Exception {
            api.loadBoard(board, page);
        }
    }

    public BoardReader(Api api, Midlet midlet, String board, int page) {
        super(api, midlet);
        this.board = board;
        this.page = page;
        this.midlet = midlet;
        
        setAttachmentsThumbnailLoader(new CachedAttachmentsThumbnailLoader(new SimpleDownloader(api), new FSCache("/root/dobrochan")));
    }

    public void changeBoard(String board) {
        this.board = board;
        loadBoard();
    }
    
    protected void loadBoard() {
        thread_offset = 0;
        getAttachmentsThumbnailLoader().free();
        threads = new Vector();
        threads_posts = new Vector();
        resolve_thread = new BoardResolveThread(api, board, page);
        resolve_thread.start();
    }

    protected void updateThread() {
        post_offset = 0;

        try {
            posts = (Vector) threads_posts.elementAt(thread_offset);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    protected void control(int keyCode, int state) {
        super.control(keyCode, state);

        if (state == 1) {
            System.out.println(keyCode);
            if (keyCode == -5) {
                ThreadReader r = new ThreadReader(
                        api,
                        midlet,
                        board,
                        ((BoardThread) threads.elementAt(thread_offset)).getId(),
                        this,
                        getAttachmentsThumbnailLoader()
                );
                midlet.changeDisplayable(r);
            }
            
            if (keyCode == 49) {
                if (thread_offset > 0) {
                    thread_offset--;
                    updateThread();

                }
            }
            if (keyCode == 51) {
                if (thread_offset < threads_posts.size() - 1) {
                    thread_offset++;
                    updateThread();
                }
            }

            if (keyCode == 52) {
                if (page > 0) {
                    page--;
                }

                loadBoard();
            }

            if (keyCode == 54) {
                page++;

                loadBoard();
            }

            if (keyCode == 35) {
                loadBoard();
            }

            if (keyCode == 42) {
                midlet.changeDisplayable(new ChangeBoardForm(midlet, this));
            }

            // 42, 48, 35
            // 49, 50, 51
        }
    }

    protected void init() {
        super.init();
        loadBoard();
    }

    protected void paint(Graphics g) {
        super.paint(g);
        drawBar(g);
        repaint();
    }

    protected void drawBar(Graphics g) {
        super.drawBar(g);
        g.drawString(board + "/" + page + ":" + (thread_offset + 1) + "/" + threads_posts.size(), 0, 0, 0);
    }

    public void resolved(BoardThread t) {
        Vector posts = new Vector();
        for (int i = 0; i < t.getPosts().size(); i++) {
            posts.addElement(new ViewablePost((BoardPost) t.getPosts().elementAt(i), font, getWidth()));
        }

        threads_posts.addElement(posts);
        threads.addElement(t);

        if (threads_posts.size() == 1) {
            updateThread();
        }
    }
}
