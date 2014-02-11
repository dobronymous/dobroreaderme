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
public class Settings {

    public static Colorscheme[] colorschemes = new Colorscheme[] {
        
    };
    
    public static int colorscheme = 0;
    
    
    public static final int CACHE_JPG = 1;
    public static final int CACHE_PNG = 2;
    public static final int NO_CACHE = 0;
    
    public static int cache_type = 1;
    
    public static String cache_path = "file:///root/dobrochan/"; //"file:///c:/user/other/dobrochan/";
    
    public static long max_mem = 4000000;
}
