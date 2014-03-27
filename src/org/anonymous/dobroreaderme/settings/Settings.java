/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.settings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

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

    public static int max_mem = -1;

    public static boolean load_all_images = false;

    public static String password = "1";
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
        cookies.put(cookie.substring(0, eq_pos), cookie.substring(eq_pos + 1, cookie.length()));
    }

    public static Colorscheme color() {
        return colorschemes[colorscheme];
    }

    public static void store() throws RecordStoreException, IOException {
        ByteArrayOutputStream d = new ByteArrayOutputStream();
        RecordStore s = RecordStore.openRecordStore("general", true);

        int[] integers = {
            colorscheme, cache_type, max_mem
        };

        String[] strings = {
            getCookies(), cache_path
        };

        for (int i = 0; i < integers.length; i++) {
            byte[] data = intToByteArray(integers[i]);
            s.addRecord(data, 0, data.length);
        }

        for (int i = 0; i < strings.length; i++) {
            byte[] data = strings[i].getBytes();
            s.addRecord(data, 0, data.length);
        }

        s.closeRecordStore();
    }

    public static void restore() throws RecordStoreException, IOException {
        RecordStore s = RecordStore.openRecordStore("general", true);

        int i = 1;
        colorscheme = fromByteArray(s.getRecord(i++));
        cache_type = fromByteArray(s.getRecord(i++));
        max_mem = fromByteArray(s.getRecord(i++));
        byte[] data = s.getRecord(i++);
        if (data != null) {
            String cookies = new String(s.getRecord(i++));
            int pos = 0;
            while ((pos = cookies.indexOf(';')) != -1) {
                String cookie = cookies.substring(0, pos);
                setCookie(cookie + "; ");
                cookies = cookies.substring(pos + 1, cookies.length());
            }
        }
        data = s.getRecord(i++);
        if (data != null)
            cache_path = new String(data);

        s.closeRecordStore();
    }

    private static final byte[] intToByteArray(int value) {
        return new byte[]{
            (byte) (value >>> 24),
            (byte) (value >>> 16),
            (byte) (value >>> 8),
            (byte) value};
    }

    protected static int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }
}
