/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.networking.util;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author sp
 */
public class StreamUtil {
    public static String readUntilEndswith(InputStream is, String endswith) throws IOException {
        StringBuffer b = new StringBuffer();
        int ch;
        while ((ch = is.read()) != -1) {
            b.append((char) ch);

            if (b.toString().endsWith(endswith)) {
                break;
            }
        }
        
        return b.toString();
    }
    
    public static void skipUntilEndswith(InputStream is, String endswith) throws IOException {
        StringBuffer b = new StringBuffer();
        int ch;
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
            
            if (b.length() > endswith.length()) {
                b.delete(0, b.length() - endswith.length());
            }
            
            if (b.toString().endsWith(endswith)) {
                break;
            }
        }
    }
    
    public static int countMatching(char match_plus, char match_minus, int initial, char ch) {
        if (ch == match_plus) {
            return initial+1;
        }
        if (ch == match_minus)
            return initial-1;
        return initial;
    }
}
