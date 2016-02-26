/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JMeter.plugins.functional.samplers.geoevent;

import java.awt.BorderLayout;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author davi5017
 */
public class HttpJsonToStreamServiceSamplerGui extends AbstractSamplerGui {

    private HttpJsonToStreamServiceSamplerPanel httpJsonToStreamServiceSamplerPanel;
    private static final Logger log = LoggingManager.getLoggerForClass();

    public HttpJsonToStreamServiceSamplerGui() {
        super();
        init();
        initFields();

        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);
        add(httpJsonToStreamServiceSamplerPanel, BorderLayout.CENTER);

    }

    @Override
    public String getStaticLabel() {
        return "Http Json to Stream Service Sampler";
    }

    @Override
    public String getLabelResource() {
        throw new IllegalStateException("getLabelResource shouldn't be called");
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof HttpJsonToStreamServiceSampler) {
            HttpJsonToStreamServiceSampler httpJsonToStreamServiceSamplerTestElement = (HttpJsonToStreamServiceSampler) element;
            httpJsonToStreamServiceSamplerPanel.setInputRestUrl(httpJsonToStreamServiceSamplerTestElement.getRestInputURL());
            httpJsonToStreamServiceSamplerPanel.setOutputStreamServiceURL(httpJsonToStreamServiceSamplerTestElement.getStreamServiceURL());
            httpJsonToStreamServiceSamplerPanel.setIDFieldName(httpJsonToStreamServiceSamplerTestElement.getIDFieldName());
            httpJsonToStreamServiceSamplerPanel.setPostBody(httpJsonToStreamServiceSamplerTestElement.getPostBody());
            httpJsonToStreamServiceSamplerPanel.setTimeout(httpJsonToStreamServiceSamplerTestElement.getTimeout());
            httpJsonToStreamServiceSamplerPanel.setStep(httpJsonToStreamServiceSamplerTestElement.getStep());
        }
    }

    @Override
    public TestElement createTestElement() {
        HttpJsonToStreamServiceSampler preproc = new HttpJsonToStreamServiceSampler();
        configureTestElement(preproc);
        return preproc;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        if (te instanceof HttpJsonToStreamServiceSampler) {
            HttpJsonToStreamServiceSampler httpJsonToStreamServiceSampler = (HttpJsonToStreamServiceSampler) te;
            httpJsonToStreamServiceSampler.setRestInputURL(httpJsonToStreamServiceSamplerPanel.getInputRestUrl());
            httpJsonToStreamServiceSampler.setStreamServiceURL(httpJsonToStreamServiceSamplerPanel.getOutputStreamServiceURL());
            httpJsonToStreamServiceSampler.setIDFieldName(httpJsonToStreamServiceSamplerPanel.getIDFieldName());
            httpJsonToStreamServiceSampler.setPostBody(httpJsonToStreamServiceSamplerPanel.getPostBody());
            httpJsonToStreamServiceSampler.setTimeout(httpJsonToStreamServiceSamplerPanel.getTimeout());
            httpJsonToStreamServiceSampler.setStep(httpJsonToStreamServiceSamplerPanel.getStep());

        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void init() {
        httpJsonToStreamServiceSamplerPanel = new HttpJsonToStreamServiceSamplerPanel();
    }

    private void initFields() {
        httpJsonToStreamServiceSamplerPanel.initFields();
    }

}
