/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.ui.forms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;
import org.anonymous.dobroreaderme.reader.BoardReader;

/**
 *
 * @author sp
 */
public class ReplyForm extends Form implements CommandListener {

    protected TextField message = new TextField("Message", "", 9000, TextField.ANY);
    protected ImageItem captcha_image = new ImageItem("Captcha", null, ImageItem.LAYOUT_DEFAULT, null);
    protected TextField captcha = new TextField("Captcha", "", 9000, TextField.ANY);

    protected Command submit = new Command("Submit", Command.SCREEN, 1);
    protected Command back = new Command("Back", Command.BACK, 1);

    protected Midlet midlet;
    protected Displayable reader;
    protected Api api;
    protected String board;
    protected BoardThread thread;

    public ReplyForm(Midlet midlet, Displayable reader, Api api, String board, BoardThread thread) {
        super("Reply");
        this.midlet = midlet;
        this.reader = reader;
        this.api = api;
        this.board = board;
        this.thread = thread;
        
        try {
            captcha_image.setImage(api.loadImage("captcha/" + board + "/" + System.currentTimeMillis() + ".png", null));
        } catch (ResolveErrorException ex) {
            ex.printStackTrace();
        }

        append(message);
        append(captcha_image);
        append(captcha);
        this.addCommand(back);
        this.addCommand(submit);
        this.setCommandListener(this);
        System.out.println("construct");
    }

    public void commandAction(Command c, Displayable d) {
        if (c == submit) {
            api.reply(board, thread, "Anon", message.getString(), "Subj", captcha.getString());
        }

        if (c == back) {
            midlet.changeDisplayable(reader);
        }
    }
}
