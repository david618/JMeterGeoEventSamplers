/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JMeter.plugins.functional.samplers.geoevent;


import java.util.concurrent.ConcurrentLinkedQueue;
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
    
    static ConcurrentLinkedQueue<Integer> ids; 
    
    public boolean hasId(int id) {
        boolean f = false;
        //int n = ids.indexOf(id);
        if (ids.contains((Integer) id)) {
            boolean a = ids.remove((Integer) id);
            f = true;     
            //System.out.println(id + " " + a);
        }
        return f;
    }
    
    public boolean isReady() {
        return ready;
    }

    public WsListener(String idFieldName) {
        this.idFieldName = idFieldName;
        ids = new ConcurrentLinkedQueue<Integer>();
        ready = false;
    }


    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String s) {
//        System.out.println(s);
        // Parse the String and find the id 
        JSONObject json = new JSONObject(s);
        ids.add(json.getJSONObject("attributes").getInt(idFieldName));

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

    }

    @Override
    public void onWebSocketError(Throwable throwable) {

    }

}
