/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.reader;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.attach.AttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.networking.attach.CachedAttachmentsThumbnailLoader;
import org.anonymous.dobroreaderme.settings.Settings;
import org.anonymous.dobroreaderme.ui.ViewablePost;
import org.anonymous.dobroreaderme.ui.menu.AttachmentItem;
import org.anonymous.dobroreaderme.ui.menu.Item;
import org.anonymous.dobroreaderme.ui.menu.Menu;
import org.anonymous.dobroreaderme.ui.menu.PostHeader;

/**
 *
 * @author sp
 */
public class PostsReader extends Reader {

    // attachments
    protected AttachmentsThumbnailLoader image_loader;
    protected int attachments_offset, attachment_loading_connecting_ticker = 0;

    // menu
    protected Menu menu = new Menu();
    protected Item current_item = null;
    protected int menu_last_update, menu_update, attachments_update_last_post;

    // posts
    protected Vector posts;
    protected int post_offset, post_index;
    protected int margin = 4;
    protected int posts_paint_offset = 0, posts_paint_end = 0, posts_paint_top = 0;

    // images
    protected boolean load_image = false, load_all_images = Settings.load_all_images;

    public PostsReader(Api api, Midlet midlet) {
        super(api, midlet);
    }

    public void setAttachmentsThumbnailLoader(AttachmentsThumbnailLoader image_loader) {
        this.image_loader = image_loader;
    }

    public AttachmentsThumbnailLoader getAttachmentsThumbnailLoader() {
        return image_loader;
    }

    protected int postHeight(ViewablePost p) {
        int offset = 0;
        offset += font_height;
        offset += p.getMessageHeight(font_height);
        offset += p.getSubjectHeight(font_height);
        offset += p.getAttachmentsHeight();
        offset += this.margin;
        return offset;
    }

    protected void scrollTo(int scroll_to_index) {
        int offset = 0;
        for (int i = 0; i < scroll_to_index; i++) {
            try {
                offset += postHeight((ViewablePost) posts.elementAt(i));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        post_offset = -offset;
    }

    protected boolean isMenuNeedsUpdate() {
        return (menu_last_update != (post_offset + attachments_offset + menu_update));
    }

    protected void updateMenu() {
        menu_last_update = (post_offset + attachments_offset + menu_update);
    }

    protected void init() {
        super.init();

        if (!image_loader.isAlive()) {
            image_loader.start();
        }
    }

    protected void control(int keyCode, int state) {
        if (state == 1 || state == 2) {
            if (keyCode == -1) {
                post_offset += 50;
            }
            if (keyCode == -2) {
                post_offset -= 50;
            }
            if (keyCode == -3) {
                attachments_offset += 30;
            }
            if (keyCode == -4) {
                attachments_offset -= 30;
            }

            if (keyCode == 50) {
                post_offset += getHeight() - font_height*2;
            }
            if (keyCode == 56) {
                post_offset -= getHeight() - font_height*2;
            }

            if (keyCode == -5) {
                if (current_item != null) {
                    current_item.pull();
                }
            }
        }

        if (state == 1) {
            if (keyCode == 55) {
                load_image = true;
            }
            if (keyCode == 57) {
                Settings.load_all_images = !Settings.load_all_images;
            }
            if (keyCode == 48) {
                getAttachmentsThumbnailLoader().free();
            }
        }
    }

    protected void update() throws Exception {
        super.update();

        if (isMenuNeedsUpdate()) {
            menu.clearItems();
            current_item = null;
        }

        if (attachments_update_last_post != post_index) {
            attachments_offset = 0;
            attachments_update_last_post = post_index;
        }

        if (posts != null) {
            post_index = posts_paint_offset;
            int offset = post_offset + font_height;
            posts_paint_offset = -1;
            posts_paint_end = 0;

            ViewablePost p = null;
            for (int i = 0; i < posts.size(); i++) {
                p = (ViewablePost) posts.elementAt(i);

                int post_height = postHeight(p);

                if (posts_paint_offset == -1 && offset > -post_height) {
                    posts_paint_offset = i;
                    posts_paint_top = offset;
                }

                if (offset < getHeight()) {
                    posts_paint_end++;
                }

                if (offset > 0 - post_height && offset < getHeight()) {
                    int x;
                    if (i == 0) { // op post, no rect, no left-padding
                        x = 1;
                    } else { // ordinary post, fill rect, draw outline, have fun
                        x = 4;
                    }

                    offset += font_height;
                    offset += p.getSubjectHeight(font_height);

                    // attachments
                    if (!p.getAttachments().isEmpty()) {
                        int local_attachments_offset = attachments_offset + x + 1;
                        for (int n = 0; n < p.getAttachments().size(); n++) {
                            BoardAttachment a = (BoardAttachment) p.getAttachments().elementAt(n);

                            if (a.getLoadingState() == 0) { // if downloading not started
                                if (Settings.load_all_images) { // or if load all images turned on
                                    image_loader.addTask(a);
                                } else if (getAttachmentsThumbnailLoader() instanceof CachedAttachmentsThumbnailLoader) {
                                    CachedAttachmentsThumbnailLoader c = (CachedAttachmentsThumbnailLoader) getAttachmentsThumbnailLoader();
                                    if (c.getCache().exists(a)) { // and image exists in cache
                                        image_loader.addTask(a);
                                    }
                                }

                            }

                            if (isMenuNeedsUpdate()) {
                                AttachmentItem item = new AttachmentItem(local_attachments_offset, offset, a, getAttachmentsThumbnailLoader());
                                menu.addItem(item);
                            }

                            if (a.getLoadingState() == 2) {
                                local_attachments_offset += a.getThumbnail().getWidth();
                            } else {
                                local_attachments_offset += a.getThumbHeight();
                            }
                        }

                    }
                    offset += p.getAttachmentsHeight();
                    offset += p.getMessageHeight(font_height);
                    offset += this.margin;
                } else {
                    offset += postHeight(p);
                }
            }

            load_image = false;
        }

        if (isMenuNeedsUpdate()) {
            current_item = menu.getCursorFor(getWidth() / 6, getHeight() / 4);
        }

        updateMenu();
    }

    protected void paint(Graphics g) {
        super.paint(g);

        if (posts_paint_offset != -1) {
            int offset = posts_paint_top;

            for (int i = posts_paint_offset; i < posts_paint_end; i++) {
                ViewablePost p = (ViewablePost) posts.elementAt(i);

                int x;
                if (i == 0) { // op post, no rect, no left-padding
                    x = 1;
                } else { // ordinary post, fill rect, draw outline, have fun
                    x = 3;
                    g.setColor(Settings.color().post_outline);
                    g.fillRoundRect(x, offset, g.getClipWidth() - 3 * 2, postHeight(p) - margin, 15, 15);
                    g.setColor(Settings.color().post_background);
                    g.drawRoundRect(x, offset, g.getClipWidth() - 3 * 2, postHeight(p) - margin, 15, 15);
                    x = 4;
                }

                // header
                g.setColor(Settings.color().post_foreground);
                g.drawString("#" + p.getId(), x + 1, offset, 0);
                g.drawString(p.getName(), g.getClipWidth() - x - 1, offset, Graphics.TOP | Graphics.RIGHT);
                offset += font_height;

                // subject
                if (!p.getSubjectLines().isEmpty()) {
                    for (int n = 0; n < p.getSubjectLines().size(); n++) {
                        g.drawString(p.getSubjectLines().elementAt(n).toString(), x, offset, 0);
                        offset += font_height;
                    }
                }

                // attachments
                if (!p.getAttachments().isEmpty()) {
                    int local_attachments_offset = attachments_offset + x + 1;
                    for (int n = 0; n < p.getAttachments().size(); n++) {
                        BoardAttachment a = (BoardAttachment) p.getAttachments().elementAt(n);

                        if (a.getLoadingState() == 1) { // currently loading
                            g.setColor(Settings.color().post_image_loading_arc);
                            int 
                                    arc_x = (int) (local_attachments_offset + a.getThumbHeight() * 0.10),
                                    arc_y = (int) (offset + a.getThumbHeight() * 0.10),
                                    arc_h = (int) (a.getThumbHeight() * 0.80), 
                                    arc_w = (int) (a.getThumbHeight() * 0.80);

                            if (a.getTotal() == 0 || a.getCompleted() == 0) { // connection not estabilished
                                g.fillArc(arc_x, arc_y, arc_w, arc_h, -attachment_loading_connecting_ticker, 90);
                                // ticker
                                attachment_loading_connecting_ticker += 1;
                                if (attachment_loading_connecting_ticker > 360) {
                                    attachment_loading_connecting_ticker = 0;
                                }
                            } else { // connection ok, downloading in progress
                                int angle = (int) ((float) a.getCompleted() / a.getTotal() * (360 - 90)) + 90;
                                g.fillArc(arc_x, arc_y, arc_w, arc_h, 90 - angle - attachment_loading_connecting_ticker, angle);
                                g.setColor(Settings.color().post_image_loading_text);
                                String size = a.getCompleted() / 1024 + "/" + a.getTotal() / 1024 + " Kb";
                                g.drawString(size, arc_x + arc_w / 2, arc_y + arc_h / 2, Graphics.BASELINE | Graphics.HCENTER);
                            }
                        } else if (a.getLoadingState() == 2) { // downloading is completed
                            g.drawImage(a.getThumbnail(), local_attachments_offset, offset, 0);
                        } else { // downloading even dont started
                            g.setColor(Settings.color().post_image_not_loaded);
                            g.fillRect(local_attachments_offset, offset, a.getThumbHeight(), a.getThumbHeight());
                        }

                        if (a.getLoadingState() == 2) {
                            local_attachments_offset += a.getThumbnail().getWidth();
                        } else {
                            local_attachments_offset += a.getThumbHeight();
                        }
                    }

                    if (i != 0) { // restore outline and background
                        g.setColor(Settings.color().background);
                        g.fillRect(getWidth() - 3, offset, 3, p.getAttachmentsHeight());
                        g.fillRect(0, offset, 3, p.getAttachmentsHeight());
                        g.setColor(Settings.color().post_background);
                        g.drawLine(getWidth() - 4, offset, getWidth() - 4, offset + p.getAttachmentsHeight());
                        g.drawLine(4, offset, 4, offset + p.getAttachmentsHeight());
                        g.setColor(Settings.color().post_outline);
                        g.drawLine(getWidth() - 3, offset, getWidth() - 3, offset + p.getAttachmentsHeight());
                        g.drawLine(3, offset, 3, offset + p.getAttachmentsHeight());
                    }

                    offset += p.getAttachmentsHeight();
                }

                g.setColor(Settings.color().post_foreground);
                // message
                for (int n = 0; n < p.getMessageLines().size(); n++) {
                    if (offset < getHeight() + font_height && offset > 0) {
                        String line = p.getMessageLines().elementAt(n).toString();
                        g.drawString(line.replace('\r', ' '), x, offset, 0);
                    }
                    offset += font_height - 3;
                }

                offset += this.margin;

            }

            load_image = false;
        }

        if (current_item != null) {
            g.setColor(Settings.color().cursor_color);
            g.drawRoundRect(current_item.getX(), current_item.getY(), current_item.getW(), current_item.getH(), 10, 10);
            g.drawRoundRect(current_item.getX() - 1, current_item.getY() - 1, current_item.getW() + 2, current_item.getH() + 2, 13, 13);
        }

        updateMenu();
    }

}
