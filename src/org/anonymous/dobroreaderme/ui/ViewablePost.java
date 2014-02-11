/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.ui;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.entities.attachment.BoardImage;

/**
 *
 * @author sp
 */
public class ViewablePost {

    protected int id;
    protected Vector message_lines, subject_lines, attachments;
    protected String date, subject, name;

    public ViewablePost(BoardPost p, Font f, int width) {
        id = p.getId();
        message_lines = ViewablePost.format(p.getMessage(), f, width);
        date = p.getDate();
        subject_lines = ViewablePost.format(p.getSubject(), f, width);
        name = p.getName();
        attachments = p.getAttachments();
    }

    public int getId() {
        return id;
    }

    public Vector getMessageLines() {
        return message_lines;
    }

    public Vector getSubjectLines() {
        return subject_lines;
    }

    public int getMessageHeight(Font f) {
        return f.getHeight() * message_lines.size();
    }

    public int getSubjectHeight(Font f) {
        return f.getHeight() * subject_lines.size();
    }

    public int getAttachmentsHeight() {
        int max = 0;
        for (int i = 0; i < getAttachments().size(); i++) {
            int height = ((BoardAttachment) getAttachments().elementAt(i)).getThumbHeight();
            if (height > max) {
                max = height;
            }
        }

        return max;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Vector getAttachments() {
        return attachments;
    }

    public static Vector format(String message, Font f, int width) {
        Vector lines = new Vector(message.length() / 20);

        String line = "";
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (ch == '\n') {
                if (!line.endsWith("\r")) {
                    line = line + "\r";
                }
                lines.addElement(trans(line));
                line = "";
            } else if (f.stringWidth(line + ch) >= width) {
                lines.addElement(trans(line));
                line = "" + ch;
            } else {
                line = line + ch;
            }
        }

        if (line != "") {
            lines.addElement(trans(line));
        }

        return lines;
    }

    public static String trans(String s) {
        return s;

        /*
         char rus[] = {
         'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
         };

         char eng[] = {
         'A', 'B', 'V', 'G', 'D', 'E', 'E', 'Z', 'Z', 'I', 'I', 'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'T', 'U', 'F', 'H', 'C', 'C', 'S', 'S', ' ', ' ', ' ', 'E', 'U', 'Y', 'a', 'b', 'v', 'g', 'd', 'e', 'e', 'z', 'z', 'i', 'i', 'k', 'l', 'm', 'n', 'o', 'p', 'r', 's', 't', 'u', 'f', 'h', 'c', 'c', 's', 's', ' ', 'y', ' ', 'e', 'u', 'y'
         };
        
         for (int i = 0; i < eng.length; i++) {
         s = s.replace(rus[i], eng[i]);
         }
        
         return s;
         */
    }
}
