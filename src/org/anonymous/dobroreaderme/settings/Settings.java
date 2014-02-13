/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.settings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.rms.RecordStore;

/**
 *
 * @author sp
 */
public class Settings {

    public static Colorscheme[] colorschemes = new Colorscheme[]{
        new Colorscheme()
    };

    public static int colorscheme = 0;

    public static final int CACHE_JPG = 1;
    public static final int CACHE_PNG = 2;
    public static final int NO_CACHE = 0;

    public static int cache_type = NO_CACHE;

    public static String cache_path = /* "file:///root/dobrochan/"; */ "file:///c:/user/other/dobrochan/";

    public static int max_mem = 4000000;

    public static boolean load_all_images = false;

    public static Hashtable cookies = new Hashtable();

    public static String getCookies() {
        String result = "";

        if (!cookies.isEmpty()) {
            Enumeration keys = cookies.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                result += (result.length() == 0 ? "" : "; ") + key + "=" + cookies.get(key);
            }
        }

        return result;
    }

    public static void setCookie(String set_cookie_str) {
        String cookie = set_cookie_str.substring(0, set_cookie_str.indexOf(';'));
        int eq_pos = cookie.indexOf('=');
        cookies.put(cookie.substring(0, eq_pos), cookie.substring(eq_pos + 1, cookie.length() - eq_pos));
    }

    public static Colorscheme color() {
        return colorschemes[colorscheme];
    }

    public static void store() {
        ByteArrayOutputStream d = new ByteArrayOutputStream();
        RecordStore s = RecordStore.openRecordStore("general", true);

        d.write(colorscheme);
        d.write(max_mem);

        byte[] data = d.toByteArray();
        d.close();

        s.addRecord(data, 0, data.length);
        s.closeRecordStore();
    }

    public static void restore() {
        RecordStore s = RecordStore.openRecordStore("general", true);
        byte[] data = s.getRecord(0);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        colorscheme = dis.readInt();
        max_mem = dis.readInt();
        
        dis.close();
        bais.close();
        s.closeRecordStore();
    }
}
