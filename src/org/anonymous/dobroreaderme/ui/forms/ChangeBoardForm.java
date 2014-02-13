/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.ui.forms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import org.anonymous.dobroreaderme.Midlet;
import org.anonymous.dobroreaderme.reader.BoardReader;

/**
 *
 * @author sp
 */
public class ChangeBoardForm extends Form implements CommandListener {
    protected TextField board = new TextField("Board", "b", 32, TextField.ANY);
    protected Command submit = new Command("Submit", Command.SCREEN, 1);
    protected Command back = new Command("Back", Command.BACK, 1);
    
    protected Midlet midlet;
    protected BoardReader reader;
    
    public ChangeBoardForm(Midlet midlet, BoardReader reader) {
        super("Change board");
        
        this.midlet = midlet;
        this.reader = reader;
        board.setString(reader.getBoard());
        
        append(board);
        addCommand(submit);
        addCommand(back);
        setCommandListener(this);
    }
    
    public void commandAction(Command command, Displayable displayable) {
        if (command == submit) {
            reader.changeBoard(board.getString());
            midlet.changeDisplayable(reader);
        }
        
        if (command == back) {
            midlet.changeDisplayable(reader);
        }
    }
}
