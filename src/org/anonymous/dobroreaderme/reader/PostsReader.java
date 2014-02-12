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

/**
 *
 * @author sp
 */
public class PostsReader extends Reader {

    protected AttachmentsThumbnailLoader image_loader;
    protected Vector posts;
    protected int post_offset, attachments_offset, post_index;
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

    protected void init() {
        super.init();

        if (!image_loader.isAlive()) {
            image_loader.start();
        }
    }

    protected void control(int keyCode, int state) {
        if (state == 1 || state == 2) {
            if (keyCode == -1) {
                post_offset += 30;
                attachments_offset = 0;
            }
            if (keyCode == -2) {
                post_offset -= 30;
                attachments_offset = 0;
            }
            if (keyCode == -3) {
                attachments_offset += 30;
            }
            if (keyCode == -4) {
                attachments_offset -= 30;
            }

            if (keyCode == 50) {
                post_offset += getHeight() - font_height;
            }

            if (keyCode == 56) {
                post_offset -= getHeight() + font_height;
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

    protected void paint(Graphics g) {
        super.paint(g);

        if (posts != null) {
            post_index = 0;
            int margin = 4;
            int offset = post_offset + font_height;
            for (int i = 0; i < posts.size(); i++) {
                ViewablePost p = (ViewablePost) posts.elementAt(i);

                int post_height = font_height + p.getMessageHeight(font) + p.getSubjectHeight(font) + p.getAttachmentsHeight();

                if (offset < g.getClipHeight()) {
                    post_index++;
                } else {
                    break;
                }

                if (offset > 0 - post_height && offset < g.getClipHeight()) {
                    int x;

                    if (i == 0) {
                        x = 1;
                    } else {
                        x = 3;
                        g.setColor(240, 224, 214);
                        g.fillRoundRect(x, offset, g.getClipWidth() - 3 * 2, font_height + p.getMessageHeight(font) + p.getSubjectHeight(font) + p.getAttachmentsHeight(), 15, 15);
                        g.setColor(221, 204, 197);
                        g.drawRoundRect(x, offset, g.getClipWidth() - 3 * 2, font_height + p.getMessageHeight(font) + p.getSubjectHeight(font) + p.getAttachmentsHeight(), 15, 15);
                        x = 4;
                    }

                    // rectangle
                    g.setColor(96, 0, 0);
                    // header
                    g.drawString("#" + p.getId() + " " + p.getName(), x + 1, offset, 0);
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
                        int local_attachments_offset = attachments_offset + x;
                        for (int n = 0; n < p.getAttachments().size(); n++) {
                            BoardAttachment a = (BoardAttachment) p.getAttachments().elementAt(n);
                            if (a.getLoadingState() == 0) {
                                if (getAttachmentsThumbnailLoader() instanceof CachedAttachmentsThumbnailLoader) {
                                    CachedAttachmentsThumbnailLoader c = (CachedAttachmentsThumbnailLoader) getAttachmentsThumbnailLoader();
                                    if (c.getCache().exists(a)) {
                                        image_loader.addTask(a);
                                    }
                                }
                                 
                                if (load_image || load_all_images) {
                                    image_loader.addTask(a);
                                }
                            }

                            if (a.getLoadingState() == 1) {
                                g.setColor(255, 255, 255);
                                g.drawRect(local_attachments_offset, offset, a.getThumbHeight(), a.getThumbHeight());
                            } else if (a.getLoadingState() == 2) {
                                g.drawImage(a.getThumbnail(), local_attachments_offset, offset, 0);
                            } else {
                                g.setColor(96, 0, 0);
                                g.drawRect(local_attachments_offset, offset, a.getThumbHeight(), a.getThumbHeight());
                            }

                            if (a.getLoadingState() == 2) {
                                local_attachments_offset += a.getThumbnail().getWidth();
                            } else {
                                local_attachments_offset += a.getThumbHeight();
                            }
                        }

                        if (i != 0) { // restore outline and background
                            g.setColor(255, 255, 255);
                            g.fillRect(getWidth() - 3, offset, 3, p.getAttachmentsHeight());
                            g.fillRect(0, offset, 3, p.getAttachmentsHeight());
                            g.setColor(240, 224, 214);
                            g.drawLine(getWidth() - 4, offset, getWidth() - 4, offset + p.getAttachmentsHeight());
                            g.drawLine(4, offset, 4, offset + p.getAttachmentsHeight());
                            g.setColor(221, 204, 197);
                            g.drawLine(getWidth() - 3, offset, getWidth() - 3, offset + p.getAttachmentsHeight());
                            g.drawLine(3, offset, 3, offset + p.getAttachmentsHeight());
                        }
                        offset += p.getAttachmentsHeight();
                    }

                    g.setColor(96, 0, 0);
                    // message
                    for (int n = 0; n < p.getMessageLines().size(); n++) {
                        String line = p.getMessageLines().elementAt(n).toString();
                        if (line.startsWith(">") && !line.startsWith(">>")) {
                            g.setColor(120, 153, 34);
                        }

                        g.drawString(line.replace('\r', ' '), x, offset, 0);

                        if (line.endsWith("\r") || line.trim().equals(" ")) {
                            g.setColor(96, 0, 0);
                        }

                        offset += font_height;
                    }

                    offset += margin;
                } else {
                    offset += font_height;
                    offset += p.getMessageHeight(font);
                    offset += p.getSubjectHeight(font);
                    offset += p.getAttachmentsHeight();
                    offset += margin;
                }
            }

            load_image = false;
        }
    }

}
