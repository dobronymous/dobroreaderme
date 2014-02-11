/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.ui;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import org.anonymous.dobroreaderme.Midlet;

/**
 *
 * @author sp
 */
public class PopupForm extends Form implements CommandListener {
    protected Displayable back;
    protected Midlet midlet;
    protected Command backCommand = new Command("Back", Command.BACK, 0);
    
    public PopupForm(String[] strings, Midlet midlet, Displayable back) {
        super("");
        this.back = back;
        this.midlet = midlet;

        for (int i = 0; i < strings.length; i++) {
            append((String) strings[i]);
        }
        
        addCommand(backCommand);
        setCommandListener(this);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == backCommand) {
            midlet.changeDisplayable(back);
        }
    }
}
