#gserv-test
==========

Test harness for unit testing gserv actions.

## Testing Resources
This package includes a class 'ResourceTester' that can be used for unit testing Resource Actions.


### Usage
<pre>
import groovyx.gpars.dataflow.Promise
import io.github.javaconductor.gserv.GServ
import io.github.javaconductor.gserv.GServFactory
import io.github.javaconductor.gserv.GServResource
import io.github.javaconductor.gserv.configuration.GServConfig
import io.github.javaconductor.gserv.tester.InstanceTester
import org.junit.Assert
import org.junit.Test

//// Sample Resource to unit test
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
    void testPromiseSample() {

        def gServFactory = new GServFactory()
        def cfg = gServFactory.createGServConfig(res.actions)

        InstanceTester t = new InstanceTester(cfg)
        Promise p = t.run("GET", [:], "/app", null)
        p.then { resp ->
            def code = resp.statusCode
            def headers = resp.responseHeaders
            def outputStr = new String(resp.output)
            assert  (outputStr.equals("Hello"))
        }// run
        println p.get()

    }


</pre>




