/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.entities;

import java.util.Vector;

/**
 *
 * @author sp
 */
public class BoardThread {
    protected int id, posts_count;
    protected Vector posts;
    
    public BoardThread(int id, int posts_count, Vector posts) {
        this.id = id;
        this.posts = posts;
        this.posts_count = posts_count;
    }

    public Vector getPosts() {
        return posts;
    }

    public void setPosts(Vector posts) {
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public int getPostsCount() {
        return posts_count;
    }
}
