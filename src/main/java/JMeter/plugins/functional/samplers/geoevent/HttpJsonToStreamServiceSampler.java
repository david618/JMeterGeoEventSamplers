/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JMeter.plugins.functional.samplers.geoevent;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.json.JSONObject;

/**
 *
 * @author davi5017
 */
public class HttpJsonToStreamServiceSampler extends AbstractSampler implements TestStateListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static ExecutorService executor = Executors.newCachedThreadPool();
    

    
    static WsClient wsClient;
    static Thread readerThread;

    public HttpJsonToStreamServiceSampler() {
        super();
        setName("HttpJsonToStreamServiceSampler");
    }

    public String getRestInputURL() {
        return getPropertyAsString("restInputURL", "");
    }

    public void setRestInputURL(String url) {
        setProperty("restInputURL", url);
    }

    public String getStreamServiceURL() {
        return getPropertyAsString("streamServiceURL", "");
    }

    public void setStreamServiceURL(String url) {
        setProperty("streamServiceURL", url);
    }

    public String getIDFieldName() {
        return getPropertyAsString("IDFieldName", "");
    }

    public void setIDFieldName(String id) {
        setProperty("IDFieldName", id);
    }

    public String getPostBody() {
        return getPropertyAsString("PostBody", "");
    }

    public void setPostBody(String postBody) {
        setProperty("PostBody", postBody);
    }

    public String getTimeout() {
        return getPropertyAsString("Timeout", "");
    }

    public void setTimeout(String timeout) {
        setProperty("Timeout", timeout);
    }    
    
    public String getStep() {
        return getPropertyAsString("Step", "");
    }

    public void setStep(String step) {
        setProperty("Step", step);
    }        
    
    @Override
    public SampleResult sample(Entry entry) {
        
//        System.out.println("Start of Sample");

        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel(getName());

        int timeout = Math.abs(Integer.parseInt(getTimeout()));
        int step = Math.abs(Integer.parseInt(getStep()));
        
        JSONObject json = new JSONObject(getPostBody());
        int id = json.getInt(getIDFieldName());
//        System.out.println(id);
        
        
        //log.info("Start of Sample");
        sampleResult.sampleStart();

//        System.out.println(getRestInputURL());
//        System.out.println(getStreamServiceURL());
//        System.out.println(getIDFieldName());
//        System.out.println(getPostBody());

        /*
         try {
         Thread.sleep(1000L);
         } catch (InterruptedException ex) {
         java.util.logging.Logger.getLogger(HttpJsonToStreamServiceSampler.class.getName()).log(Level.SEVERE, null, ex);
         }        
         */
        try {


            
            // Send the Message to Rest Endpoint
            String urlString = getRestInputURL();
            //System.out.println(urlString);

            URL url = new URL(urlString);

            Client client;

            if (url.getProtocol().equalsIgnoreCase("https")) {
                TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
                };
                SSLContext sc = null;

                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                client = ClientBuilder.newBuilder().sslContext(sc).build();
            } else {
                client = ClientBuilder.newClient();
            }

            WebTarget target = client.target(urlString);
            
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            
            Response response = invocationBuilder.post(Entity.entity(getPostBody(), MediaType.APPLICATION_JSON));
            
                        

//            WebTarget targetWithQueryParams = target.queryParam("f", "json");
//
//            Invocation.Builder invocationBuilder = targetWithQueryParams.request(MediaType.TEXT_PLAIN_TYPE);

//            Response response = invocationBuilder.get();
            //System.out.println(response.getStatus());

        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(HttpJsonToStreamServiceSampler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(HttpJsonToStreamServiceSampler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (KeyManagementException ex) {
            java.util.logging.Logger.getLogger(HttpJsonToStreamServiceSampler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        // Listen for Message to Appear on Stream Service
//        WsClient wsc = new WsClient(getStreamServiceURL() + "/subscribe", getUniqueMessageID());
//        
//        wsc.start();
//        
//        if (wsc.isFound()) {
//            sampleResult.setResponseCodeOK();
//        } else {
//            sampleResult.setResponseCode("999");
//        }
//        
        

        int t = 0;
        while (!wsClient.hasId(id) && t < timeout) {
            t += step;
            try {
                Thread.sleep(step);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(HttpJsonToStreamServiceSampler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        if (t < timeout) {
            sampleResult.setResponseCodeOK();
            sampleResult.setSuccessful(true);
            sampleResult.setResponseMessage("Okay");
        } else {
            sampleResult.setResponseCode("-1");
            sampleResult.setSuccessful(false);
            sampleResult.setResponseMessage("Fail");
        }
        
        sampleResult.setSamplerData(getPostBody());
        sampleResult.setBytes(getPostBody().length());

        sampleResult.sampleEnd();
        sampleResult.latencyEnd();
        

        
        //log.info("End of Sample");
        return sampleResult;

    }

    @Override
    public void testStarted() {        
        testStarted("Whatever");
    }

    @Override
    public void testStarted(String string) {
        System.out.println("Test Started");
        wsClient = new WsClient(getStreamServiceURL() + "/subscribe", getIDFieldName());
        readerThread = new Thread(wsClient);
        readerThread.start();

        while (!wsClient.isReady()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(HttpJsonToStreamServiceSampler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        log.debug("Test Started");
    }

    @Override
    public void testEnded() {
        testEnded("Whatever");
    }

    @Override
    public void testEnded(String string) {
        System.out.println("Test Ended");
        log.debug("Test Ended");
        wsClient.getListener().session.close();
        readerThread.interrupt();
        
        ConcurrentLinkedQueue<Integer> ids = wsClient.getListener().ids;
        System.out.println(ids.size());
        for (Integer id: ids) {
            System.out.print(id + " ");
        }
        System.out.println();
        
    }

}
