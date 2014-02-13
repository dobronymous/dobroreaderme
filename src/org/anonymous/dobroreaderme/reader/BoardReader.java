/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.reader;

import org.anonymous.dobroreaderme.ui.forms.ChangeBoardForm;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.attach.AttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.networking.attach.CachedAttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.cache.FSJPGCache;
import org.anonymous.dobroreaderme.cache.FSPNGCache;
import org.anonymous.dobroreaderme.networking.attach.Downloader;
import org.anonymous.dobroreaderme.networking.attach.SimpleDownloader;
import org.anonymous.dobroreaderme.networking.resolve.ResolveThread;
import org.anonymous.dobroreaderme.settings.Settings;
import org.anonymous.dobroreaderme.ui.ViewablePost;

/**
 *
 * @author sp
 */
public class BoardReader extends PostsReader {

    protected String board;
    protected int page;
    protected int thread_offset, thread_load_into;
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

    public BoardReader(Api api, Midlet midlet, String board, int page) {
        super(api, midlet);
        this.board = board;
        this.page = page;
        this.midlet = midlet;
    }

    public void changeBoard(String board) {
        this.board = board;
        loadBoard();
    }

    protected void loadBoard() {
        getAttachmentsThumbnailLoader().free();
        thread_offset = 0;
        threads = new Vector();
        threads_posts = new Vector();

        setResolveThread(new BoardResolveThread(api, board, page));
        startResolveThread();
    }

    protected void updateThread() {
        menu_update++;
        post_offset = 0;
        attachments_offset = 0;
        posts = (Vector) threads_posts.elementAt(thread_offset);
    }

    protected void control(int keyCode, int state) {
        super.control(keyCode, state);

        if (state == 1) {
            if (keyCode == -6) {
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
                BoardThread current = (BoardThread) threads.elementAt(thread_offset);
                thread_load_into = thread_offset;
                setResolveThread(new ThreadUpdateThread(api, board, current));
                startResolveThread();
            }

            if (keyCode == 42) {
                midlet.changeDisplayable(new ChangeBoardForm(midlet, this));
            }

            // 42, 48, 35
            // 49, 50, 51
        }
    }

    protected void init() {
        Downloader down = new SimpleDownloader(api);
        AttachmentsThumbnailLoader loader = null;
        switch (Settings.cache_type) {
            case Settings.NO_CACHE:
                loader = new AttachmentsThumbnailLoader(down);
                break;
            case Settings.CACHE_JPG:
                loader = new CachedAttachmentsThumbnailLoader(down, new FSJPGCache(Settings.cache_path));
                break;
            case Settings.CACHE_PNG:
                loader = new CachedAttachmentsThumbnailLoader(down, new FSPNGCache(Settings.cache_path));
                break;

        }

        setAttachmentsThumbnailLoader(loader);

        super.init();

        loadBoard();
    }

    protected void update() throws Exception {
        super.update();

        Exception e = getAttachmentsThumbnailLoader().getException();
        if (e != null) {
            getAttachmentsThumbnailLoader().removeException();
            throw e;
        }
    }

    protected void paint(Graphics g) {
        super.paint(g);
        drawBar(g);
        repaint();
    }

    protected void drawBar(Graphics g) { //@TODO: refactor
        super.drawBar(g);
        
        String board_str = board + "/" + page;
        int threads_count = 10 > threads.size() ? 10 : threads.size();

        String thread_str = (thread_offset + 1) + "/" + threads_count;
        int thread_str_len = font.stringWidth(thread_str);
        int percent = (int) Math.ceil((threads.size() * thread_str_len + 1) / (threads_count));

        int offset = 3;

        g.drawString(board_str + "", offset, 0, 0);
        offset += font.stringWidth(board_str) + 10;

        g.setColor(Settings.color().bar_progressbar_background);
        g.fillRect(offset - 3, 0, thread_str_len + 6, font_height);
        
        g.setColor(Settings.color().bar_progressbar_bar);
        g.fillRect(offset - 3, 0, percent + 6, font_height);
        
        g.setColor(Settings.color().bar_progressbar_foreground);
        g.drawString(thread_str, offset, 0, 0);
    }

    public void resolved(BoardPost p) {
        ((Vector) threads_posts.elementAt(thread_load_into)).addElement(new ViewablePost(p, font, getWidth() - 6));
    }

    public void resolved(BoardThread t) {
        Vector posts = new Vector();
        for (int i = 0; i < t.getPosts().size(); i++) {
            posts.addElement(new ViewablePost((BoardPost) t.getPosts().elementAt(i), font, getWidth() - 6));
        }

        threads_posts.addElement(posts);

        Vector only_last = new Vector(1);
        only_last.addElement(t.getPosts().lastElement());
        t.setPosts(only_last);
        threads.addElement(t);

        if (threads_posts.size() == 1) {
            updateThread();
            menu_update++;
        }
    }

    public String getBoard() {
        return board;
    }
}
