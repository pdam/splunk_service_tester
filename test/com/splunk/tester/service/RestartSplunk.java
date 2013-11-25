/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splunk.tester.service;
import com.splunk.AtomFeed;
import com.splunk.Job;
import com.splunk.ResponseMessage;
import com.splunk.Service;
import com.splunk.ServiceArgs;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pdam
 */
public class RestartSplunk extends TestCase {
    private Service service;
    
    public RestartSplunk() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    @Override
    public void setUp() {
    }
    
    @After
    @Override
    public void tearDown() {
    }
    
    private static final String hostName = "pdam-lp";
    private  static final int  port = 8089;
    private static final String scheme = "https";
    private static final String username = "admin";
    private static final String password = "admin";
    private static final String version = "5.0";
    
    @Test
    public void testLoginWithoutArguments() throws IOException {
        ServiceArgs args = new ServiceArgs();
        args.setHost(hostName);
        args.setPort(port);
        args.setScheme(scheme);
        args.setUsername(username);
        args.setPassword(password);
        service = new Service(args); 
        service.login(username, password);
        StringBuilder sb  =  new  StringBuilder();
        ResponseMessage  msg = service.restart();
        String[]  cap  = service.getCapabilities();
        
        InputStream   s =  msg.getContent();
        int  b ;
        while((b  = s.read())!=   -1 ){
             sb.append(b);       
        }
        s.close();
        
        System.out.println("Content:"+ sb.toString()+"\n");
        System.out.println("Header:"+msg.getHeader().keySet()+"\n");
        System.out.println("Status: "+ msg.getStatus()+"\n");
        for ( String c  : cap ){
            System.out.println("Capabilities:  "+ c+"\n");
        }
        
        
        service.logout();
        
    }
    
    
    // === Utility ===
    
    private static void checkResponse(ResponseMessage response) {
        assertEquals(200, response.getStatus());
        
        // Make sure we can at least load the Atom response
        AtomFeed.parseStream(response.getContent());
    }
    
    // Wait for the given job to be ready
    private static Job ready(Job job) throws InterruptedException {
        while (!job.isReady()) {
            Thread.sleep(10);
        }
        return job;
    }

   

}
