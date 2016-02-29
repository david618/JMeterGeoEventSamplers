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
public class Messages {
    static ConcurrentLinkedQueue<Integer> ids; 

    public Messages() {
        ids = new ConcurrentLinkedQueue<>();
    }
    
   
    private boolean empty = true;
    
    public synchronized boolean getID(int id, int timeout) {

        boolean f = false;
        long endtime = System.currentTimeMillis() + timeout;
        
//        System.out.println(ids);
        
        while (!ids.contains((Integer) id) && System.currentTimeMillis() < endtime) {
            try {
              
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        if (ids.contains((Integer) id)) {
            f = true;
            ids.remove((Integer) id);
        }

        notifyAll();
        return f;

    }

    public synchronized void putID(int id) {
        
     
        ids.add(id);
//        System.out.println(id);
        notifyAll();
    }    
}
