/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anonymous.dobroreaderme.networking.dobrochan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;
import org.anonymous.dobroreaderme.entities.BoardPost;
import org.anonymous.dobroreaderme.entities.BoardThread;
import org.anonymous.dobroreaderme.entities.attachment.BoardAttachment;
import org.anonymous.dobroreaderme.entities.attachment.BoardImage;
import org.anonymous.dobroreaderme.networking.Api;
import org.anonymous.dobroreaderme.networking.DownloadProgressTracker;
import org.anonymous.dobroreaderme.networking.resolve.ResolveDispatcher;
import org.anonymous.dobroreaderme.networking.resolve.ResolveErrorException;
import org.anonymous.dobroreaderme.networking.util.HTTP;
import org.anonymous.dobroreaderme.networking.aib.Dobrochan;
import org.anonymous.dobroreaderme.networking.util.StreamUtil;
import org.anonymous.dobroreaderme.settings.Settings;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author sp
 */
public class DobrochanApi implements Api {

    protected ResolveDispatcher d;
    protected Dobrochan dobrach;

    public DobrochanApi(Dobrochan dobrach) {
        this.dobrach = dobrach;
    }

    public void setDispatcher(ResolveDispatcher d) {
        this.d = d;
    }

    public void reply(String board, BoardThread thread, String name, String message, String subject, String captcha) {
        HttpConnection c = null;
        try {
            c = HTTP.openPOSTConnection(dobrach.getHost() + "/" + board + "/post/new.xhtml");
            c.setRequestProperty("Content-Type", "multipart/form-data");
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1734.0 Safari/537.36");
            HTTP.writeCookies(c, Settings.getCookies());
            
            DataOutputStream dos = c.openDataOutputStream();
            dos.writeUTF(HTTP.urlEncode(
                    "thread_id=" + thread.getId()
                    + "&task=post&name=" + name
                    + "&message=" + message
                    + "&subject=" + subject
                    + "&captcha=" + captcha
                    + "&password=" + Settings.password
                    + "&scroll_to=" + thread.getId()
                    + "&new_post=Отправить"
                    + "&post_files_count=0"
                    + "&goto=board"
            ));
            dos.flush();
            dos.close();

            InputStream is = c.openInputStream();

            System.out.println("message");
            int ch;
            while ((ch = is.read()) != -1) {
                System.out.print((char) ch);
            }
            
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public Image loadImage(String src, DownloadProgressTracker tracker) throws ResolveErrorException {
        Image image = null;
        HttpConnection conn = null;
        InputStream is = null;

        try {
            conn = HTTP.openGETConnection(imagePath(src));
            if (tracker != null)
                tracker.setTotal(conn.getLength());
            is = conn.openInputStream();

            byte[] image_data = new byte[(int) conn.getLength()]; //@TODO: is this vunerable?
            byte[] buffer = new byte[256];
            int readed, total = 0;
            while ((readed = is.read(buffer)) != -1) {
                System.arraycopy(buffer, 0, image_data, total, readed);
                total += readed;
                
                if (tracker != null)
                    tracker.setCompleted(total);
            }

            is.close();
            conn.close();

            image = Image.createImage(image_data, 0, total);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResolveErrorException("IOException caused during image download: " + e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return image;
    }

    public String imagePath(String src) throws ResolveErrorException {
        return dobrach.getHost() + "/" + src;
    }

    public void loadThread(String board, int id) throws ResolveErrorException {
        resolveThread(dobrach.getHost() + "/api/thread/" + board + "/" + id + "/all.json", true, true);
    }

    public void updateThread(String board, BoardThread thread) throws ResolveErrorException {
        int last_post = ((BoardPost) thread.getPosts().lastElement()).getId();
        resolveThread(dobrach.getHost() + "/api/thread/" + board + "/" + thread.getId() + "/new.json?last_post=" + last_post, false, true);
    }

    public void loadBoard(String board, int page) throws ResolveErrorException {
        resolveBoard(dobrach.getHost() + "/" + board + "/" + page + ".json", true, true);
    }

    protected void resolveThread(String url, boolean send_thread, boolean send_post) throws ResolveErrorException {
        HttpConnection c = null;
        InputStream is = null;
        try {
            c = HTTP.openGETConnection(url);
            if (c.getResponseCode() != 200) {
                throw new ResolveErrorException("Server returned invalid response code: " + c.getResponseCode() + "; Message: " + c.getResponseMessage());
            }

            is = c.openInputStream();
            StringBuffer b = new StringBuffer();
            int ch;
            // skip until threads array
            String posts_array_token = ", \"posts\": [";
            String header = StreamUtil.readUntilEndswith(is, posts_array_token);
            if (!header.endsWith("}")) {
                header = header.substring(0, header.length() - posts_array_token.length()) + "}";
            }

            BoardThread t = parseThreadHeader(new JSONObject(header));
            if (send_thread) {
                d.resolved(t);
            }

            // read threads
            int braces = 0, brackets = 1; // one bracket we're already skipped
            while ((ch = is.read()) != -1) {
                // match brackets and braces
                brackets = StreamUtil.countMatching('[', ']', brackets, (char) ch);
                braces = StreamUtil.countMatching('{', '}', braces, (char) ch);

                // post brace opened
                if (braces >= 1) {
                    b.append((char) ch);
                }

                // post brace closed
                if (braces == 0 && b.toString().startsWith("{")) {
                    // add matching } to the end, parse, and send
                    if (send_post) {
                        d.resolved(parsePost(new JSONObject(b.toString() + "}")));
                    }
                    b = new StringBuffer();
                }

                if (brackets == 0) {
                    break;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ResolveErrorException("IOException caused during resolving: " + ex.getMessage());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new ResolveErrorException("JSONException caused during resolving: " + ex.getMessage());
        } finally {
            try { // i heard you like try/cache's
                if (c != null) {
                    c.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void resolveBoard(String url, boolean send_thread, boolean send_post) throws ResolveErrorException {
        HttpConnection c = null;
        InputStream is = null;
        try {
            c = HTTP.openGETConnection(url);
            if (c.getResponseCode() != 200) {
                throw new ResolveErrorException("Server returned invalid response code: " + c.getResponseCode() + "; Message: " + c.getResponseMessage());
            }

            is = c.openInputStream();
            StringBuffer b = new StringBuffer();
            int ch;
            // skip until threads array
            StreamUtil.skipUntilEndswith(is, "\"threads\": [");

            // read threads
            int braces = 0, brackets = 1; // one bracket we're already skipped
            while ((ch = is.read()) != -1) {
                // match brackets and braces
                brackets = StreamUtil.countMatching('[', ']', brackets, (char) ch);
                braces = StreamUtil.countMatching('{', '}', braces, (char) ch);

                // if there is 1 or more braces opened 
                // its means, that its threads data, without ", " after each json array entry
                if (braces >= 1) {
                    b.append((char) ch);
                }

                // thread brace closed
                if (braces == 0 && b.toString().startsWith("{") && b.length() > 1) {
                    // add matching } to the end, parse, and send
                    d.resolved(parseThread(new JSONObject(b.toString() + "}")));
                    b = new StringBuffer();
                }

                /*if (brackets == 0) {
                 break;
                 }*/
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ResolveErrorException("IOException caused during resolving: " + ex.getMessage());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new ResolveErrorException("JSONException caused during resolving: " + ex.getMessage());
        } finally {
            try { // i heard you like try/cache's
                if (c != null) {
                    c.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected BoardThread parseThread(JSONObject thread_json) throws JSONException {
        JSONArray posts = thread_json
                .getJSONArray("posts");

        Vector postsVector = new Vector(4);
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            postsVector.addElement(parsePost(post));
        }

        BoardThread thread = parseThreadHeader(thread_json);
        thread.setPosts(postsVector);
        return thread;
    }

    protected BoardThread parseThreadHeader(JSONObject thread_json) throws JSONException {
        return new BoardThread(thread_json.getInt("display_id"), thread_json.getInt("posts_count"), new Vector());
    }

    protected BoardPost parsePost(JSONObject post) throws JSONException {
        Vector attachmentsVector = new Vector(5);

        JSONArray attachments = post.getJSONArray("files");
        for (int i = 0; i < attachments.length(); i++) {
            JSONObject file = attachments.getJSONObject(i);
            if (file.getString("type").equals("image")) {
                attachmentsVector.addElement(new BoardImage(
                        file.getString("src"),
                        file.getInt("file_id"),
                        file.getInt("size"),
                        file.getString("thumb"),
                        0,
                        0,
                        file.getInt("thumb_height")
                ));
            } else {
                attachmentsVector.addElement(new BoardAttachment(
                        file.getString("type"),
                        file.getString("src"),
                        file.getInt("file_id"),
                        file.getInt("size"),
                        file.getString("thumb"),
                        file.getInt("thumb_height")
                ));
            }
        }

        return new BoardPost(
                post.getInt("display_id"),
                post.getString("message"),
                post.getString("date"),
                post.getString("subject"),
                post.getString("name"),
                attachmentsVector
        );
    }

}
