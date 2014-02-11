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
import org.anonymous.dobroreaderme.networking.attach.SimpleDownloader;
import org.anonymous.dobroreaderme.networking.resolve.ThreadedAttachmentLoader;
import org.anonymous.dobroreaderme.ui.ViewablePost;

/**
 *
 * @author sp
 */
public class PostsReader extends Reader {

    protected AttachmentsThumbnailLoader image_loader;
    protected Vector posts;
    protected int post_offset, attachments_offset;
    protected boolean load_image = false, load_all_images = false;

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
        
        if (!image_loader.isAlive())
            image_loader.start();
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
            if (keyCode == 48) {
                load_image = true;
                //image_loader.free();
            }
            if (keyCode == 50) {
                post_offset += getHeight() - font_height;
            }
            
            if (keyCode == 56) {
                post_offset -= getHeight() + font_height;
            }
        }
    }

    protected void paint(Graphics g) {
        super.paint(g);

        if (posts != null) {
            int margin = 3;
            int offset = post_offset;
            for (int i = 0; i < posts.size(); i++) {
                ViewablePost p = (ViewablePost) posts.elementAt(i);

                int post_height = font_height + p.getMessageHeight(font) + p.getSubjectHeight(font) + p.getAttachmentsHeight();
                if (offset > 0 - post_height && offset < g.getClipHeight()) {
                    int x = 5;

                    if (i == 0) {
                        g.setColor(255, 255, 255);
                        x = 0;
                    } else {
                        g.setColor(240, 224, 214);
                        x = 3;
                    }
                    int w = g.getClipWidth() - x * 2;

                    // rectangle
                    g.fillRoundRect(x, offset, w, font_height + p.getMessageHeight(font) + p.getSubjectHeight(font) + p.getAttachmentsHeight(), 20, 20);
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
                        int local_attachments_offset = attachments_offset;
                        for (int n = 0; n < p.getAttachments().size(); n++) {
                            BoardAttachment a = (BoardAttachment) p.getAttachments().elementAt(n);
                            if (load_image && a.getLoadingState() == 0) {
                                image_loader.addTask(a);
                            }

                            if (a.getLoadingState() == 1) {
                                g.setColor(60, 60, 0);
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
        }

        if (load_image && !load_all_images) {
            load_image = false;
        }
    }

}
