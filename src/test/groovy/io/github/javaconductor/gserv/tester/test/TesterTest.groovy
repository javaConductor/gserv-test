package io.github.javaconductor.gserv.tester.test

import groovyx.gpars.dataflow.Promise
import io.github.javaconductor.gserv.GServ
import io.github.javaconductor.gserv.GServFactory
import io.github.javaconductor.gserv.GServResource
import io.github.javaconductor.gserv.configuration.GServConfig
import io.github.javaconductor.gserv.tester.InstanceTester
import io.github.javaconductor.gserv.tester.ResourceTester
import org.junit.Assert
import org.junit.Test
import java.util.Map

/**
 * Created by lcollins on 12/29/2014.
 */

class TesterTest {

    GServResource  res =  GServ.Resource("/app"){

        get ('/'){
            header("Content-Type", "text/plain")
            write "Hello"
        }
        get ('/error'){
            header("Content-Type", "text/plain")
            throw new RuntimeException("Testing exceptions handling in tests.")
//            write "Hello"
        }
    }

    @Test
    void testSample() {

        def gServFactory = new GServFactory()
        def cfg = gServFactory.createGServConfig(res.actions)

        InstanceTester t = new InstanceTester(cfg)
        t.run("GET", [:], "/app", null) { int statusCode, Map responseHeaders, byte[] output ->
            def outputStr = new String(output)
            assert outputStr == "Hello"
        }// run

    }

    @Test
    void testResourcesTester() {
        ResourceTester t = new ResourceTester(res)
        t.run("GET", [:], "/app", null) { int statusCode, Map responseHeaders, byte[] output ->
            def outputStr = new String(output)
            assert outputStr == "Hello"
        }// run
    }

    @Test
    void testSampleFailure(){

        def gServFactory = new GServFactory()
        def cfg = gServFactory.createGServConfig( res.actions )

        InstanceTester t = new InstanceTester(cfg)
        t.run("GET", [:], "/app", null ){ int statusCode, Map responseHeaders, byte[] output ->
            def outputStr = new String(output)
            assert responseHeaders['Content-Type'] == "text/plain"
            assert outputStr != "Hello World"
        }// run
    }

    @Test
    void testSampleFailure404(){

        def gServFactory = new GServFactory()
        def cfg = gServFactory.createGServConfig( res.actions )

        InstanceTester t = new InstanceTester(cfg)
        t.run("GET", [:], "/app2", null ){ int statusCode, Map responseHeaders, byte[] output ->
            assert statusCode == 404
        }// run
    }

    @Test
    void testSampleFailureWrongMethod(){

        def gServFactory = new GServFactory()
        def cfg = gServFactory.createGServConfig( res.actions )

        InstanceTester t = new InstanceTester(cfg)
        t.run("PUT", [:], "/app2", null ){ int statusCode, Map responseHeaders, byte[] output ->
            def outputStr = new String(output)
            assert statusCode == 404
        }// run
    }//test

    @Test
    void testException() {

        def gServFactory = new GServFactory()
        def cfg = gServFactory.createGServConfig(res.actions)

        InstanceTester t = new InstanceTester(cfg)
        t.run("GET", [:], "/app/error", null) { int statusCode, Map responseHeaders, byte[] output ->
            assert statusCode == 500
            def outputStr = new String(output)
            assert outputStr == "Testing exceptions handling in tests."
        }// run
    }//TEST

    @Test
    void testPromiseSample() {

        def gServFactory = new GServFactory()
        def cfg = gServFactory.createGServConfig(res.actions)

        InstanceTester t = new InstanceTester(cfg)
        Promise p = t.run("GET", [:], "/app", null)
        p.then { resp ->
            def code = resp.statusCode
            def headers = resp.responseHeaders
            def outputStr = new String(resp.output)
            assert  (outputStr.equals("Hello2"))/// TODO this is SO wrong !!! does '==' work?
        }// run
        println p.get()

    }

}
