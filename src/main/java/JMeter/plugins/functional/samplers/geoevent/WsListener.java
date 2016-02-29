/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JMeter.plugins.functional.samplers.geoevent;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

/**
 *
 * @author davi5017
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class WsListener implements WebSocketListener {

    Session session;
    String idFieldName;
    
    boolean ready;
    int cnt;
    long sttime;
    
    Messages messages;
    
    static ConcurrentLinkedQueue<Integer> ids; 
    
    public synchronized boolean getId(int id, int timeout) {
        boolean f = false;
        long endtime = System.currentTimeMillis() + timeout;
        
        while (!ids.contains((Integer) id) && System.currentTimeMillis() < endtime) {
            try {
                System.out.println(id);
                System.out.println(ids.contains((Integer) id));
                System.out.println(ids);                
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(WsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return f;
    }
    
    public synchronized boolean hasId(int id, int step) {
        boolean f = false;
        //int n = ids.indexOf(id);
        if (ids.contains((Integer) id)) {
            ids.remove((Integer) id);
            f = true;     
            //System.out.println(id + " " + a);
        } else {
            try {
                wait(step);
            } catch (InterruptedException ex) {
                Logger.getLogger(WsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return f;
    }
    
    public synchronized boolean hasId(int id) {
        boolean f = false;
        //int n = ids.indexOf(id);
        if (ids.contains((Integer) id)) {
            ids.remove((Integer) id);
            f = true;     
            //System.out.println(id + " " + a);
        } 
        return f;
    }    
    
    public boolean isReady() {
        return ready;
    }

    public WsListener(String idFieldName, Messages messages) {
        this.idFieldName = idFieldName;
        ids = new ConcurrentLinkedQueue<Integer>();
        ready = false;
        this.messages = messages;
    }


    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String s) {
        
        // Testing messages
//        System.out.println(s);
        // Parse the String and find the id 
//        cnt++;
//        if (cnt == 1) {
//            sttime = System.currentTimeMillis();
//        }
//        if (cnt == 2000) {
//           double rate = 2000.0 / (System.currentTimeMillis() - sttime) * 1000.0;
//           System.out.println(rate);
//        }
        
        JSONObject json = new JSONObject(s);
        int id = json.getJSONObject("attributes").getInt(idFieldName);
        
        // Poppulates messages (Syncrhonized Thread Wait) or ids (Polling)
        messages.putID(id);
//        ids.add(id);
    }

    @Override
    public void onWebSocketClose(int i, String s) {
//        System.out.println("Closed");
        ready = false;
        this.session = null;
        
    }

    @Override
    public void onWebSocketConnect(Session session) {
//        System.out.println("Connected");
        this.session = session;
        ready = true;
        cnt = 0;

    }

    @Override
    public void onWebSocketError(Throwable throwable) {

    }

}
