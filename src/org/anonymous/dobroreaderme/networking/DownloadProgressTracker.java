/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anonymous.dobroreaderme.networking;

/**
 *
 * @author sp
 */
public interface DownloadProgressTracker {
    public void setTotal(long bytes);
    public void setCompleted(long bytes);
    public long getCompleted();
    public long getTotal();
}
