/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.networking.attach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.DownloadProgressTracker;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;
import org.anonymous.dobroreaderme.networking.util.HTTP;

/**
 *
 * @author sp
 */
public class JPGReducerDownloader implements Downloader {

    protected Api api;

    public JPGReducerDownloader(Api api) {
        this.api = api;
    }

    public Image download(String source) throws ResolveErrorException {
        HttpConnection c = null;
        try {
            c = HTTP.openConnection("http://www.jpegreducer.com/conv.php");
            c.setRequestMethod(HttpConnection.POST);
            c.setRequestProperty("User-Agent", "Mozilla/5.0");
            c.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            c.setRequestProperty("Content-Type", "multipart/form-data");

            DataOutputStream dos = c.openDataOutputStream();
            System.out.println(HTTP.urlEncode("gifurl=" + api.imagePath(source)));
//                        dos.writeChars(HTTP.urlEncode("gifurl=" + api.imagePath(source)).toUpperCase()+"&");
            dos.writeChars(HTTP.urlEncode("gifurl=http://i.imgur.com/6g2Sw3h.jpg"));
            dos.flush();
            dos.close();

            System.out.println(c.getResponseCode());
            System.out.println(c.getResponseMessage());

            InputStream is = c.openInputStream();
            StringBuffer b = new StringBuffer();
            int ch;
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

            System.out.println(b);
        } catch (IOException e) {

        }

        return Image.createImage(1, 1);
    }

    public Image download(String source, DownloadProgressTracker tracker) throws ResolveErrorException {
        return null;
    }

}
