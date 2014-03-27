/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.entities;

import java.util.Vector;

/**
 *
 * @author sp
 */
public class BoardPost {
    protected int id;
    protected String message;
    protected String date;
    protected String subject;
    protected String name;
    protected Vector attachments;
    
    public BoardPost(int id, String message, String date, String subject, String name, Vector attachments) {
        this.id = id;
        this.message = message;
        this.subject = subject;
        this.name = name;
        this.date = date;
        this.attachments = attachments;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public String getName() {
        return name;
    }

    public Vector getAttachments() {
        return attachments;
    }
}
