/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JMeter.plugins.functional.samplers.geoevent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author davi5017
 */
public class Drop {

    static ConcurrentLinkedQueue<Integer> ids; 

    public Drop() {
        ids = new ConcurrentLinkedQueue<>();
    }
    
   
    public synchronized boolean getID(int id, int timeout) {

        boolean f = false;
        long endtime = System.currentTimeMillis() + timeout;
        
        while (!ids.contains((Integer) id) && System.currentTimeMillis() < endtime) {
            try {
              
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        notifyAll();
        return f;

    }

    public void putID(int id) {
        ids.add(id);
        notifyAll();
    }
}
