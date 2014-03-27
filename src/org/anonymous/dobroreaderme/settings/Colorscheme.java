/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.settings;

/**
 *
 * @author sp
 */
public class Colorscheme {
    public int background = color(255, 255, 255);

    public int bar_background = color(96, 0, 0);
    public int bar_foreground = color(230, 230, 230);
    public int bar_progressbar_background = color(221, 204, 197);
    public int bar_progressbar_bar = color(171, 164, 157);
    public int bar_progressbar_foreground = color(96, 0, 0);
    public int ticker_background = color(221, 204, 197);
    public int ticker_outline = color(171, 164, 157);

    public int post_outline = color(240, 224, 214);
    public int post_background = color(221, 204, 197);
    public int post_foreground = color(96, 0, 0);
    public int post_quote_foreground = color(120, 153, 34);
    public int post_image_not_loaded = color(201, 194, 187);
    public int post_image_loading_arc = color(201, 194, 187);
    public int post_image_loading_text = color(96, 0, 0);

    public int cursor_color = color(150, 0, 0);

    
    public static int color(int r, int g, int b) {
        return ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
    }
}
