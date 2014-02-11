/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking.util;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author sp
 */
public class HTTP {
      public static HttpConnection openConnection(String url) throws IOException {
          return (HttpConnection) Connector.open(url);
      }
}
