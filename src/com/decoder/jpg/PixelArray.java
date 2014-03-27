/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.decoder.jpg;

/**
 *
 * @author sp
 */
public class PixelArray implements JPGDecoder.PixelArray {
    int[] pix;
    int width, height;

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        pix = new int[width * height];
    }

    public void setPixel(int x, int y, int argb) {
        pix[x + y * width] = argb;
    }

    public int[] getPix() {
        return pix;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
