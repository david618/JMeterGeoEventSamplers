/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JMeter.plugins.functional.samplers.geoevent;

import java.net.URI;
import java.util.logging.Level;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 *
 * @author davi5017
 */
public class WsClient implements Runnable {

    String urlString;
    String idFieldName;

    boolean ready;
    
    Messages messages;

    WsListener listener;

    public WsListener getListener() {
        return listener;
    }

    public boolean hasId(int id) {
        return listener.hasId(id);
    }
    
    public boolean hasId(int id, int step) {
        return listener.hasId(id, step);
    }
    
    public boolean getId(int id, int timeout) {
        return listener.getId(id, timeout);
    }
    
       
    public boolean isReady() {
        return ready;
    }

    public WsClient(String urlString, String idFieldName, Messages messages) {
//        System.out.println(urlString);
//        System.out.println(idFieldName);
        this.urlString = urlString;
        this.idFieldName = idFieldName;
        this.ready = false;
        this.messages = messages;
    }



    @Override
    public void run() {
        try {

            URI uri = new URI(this.urlString);

            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setTrustAll(true);
            WebSocketClient webSocketClient = new WebSocketClient(sslContextFactory);

            webSocketClient.start();

            listener = new WsListener(this.idFieldName, this.messages);

            webSocketClient.connect(listener, uri);

            while (!listener.isReady()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(HttpJsonToStreamServiceSampler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.ready = true;
            
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
