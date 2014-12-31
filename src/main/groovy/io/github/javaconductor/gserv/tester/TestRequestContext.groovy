/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Lee Collins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.javaconductor.gserv.tester

import groovy.util.logging.Log4j
import io.github.javaconductor.gserv.GServ
import io.github.javaconductor.gserv.requesthandler.AbstractRequestContext
import io.github.javaconductor.gserv.requesthandler.RequestContext

/**
 * Exchange Wrapper usually used for Filters
 */
@Log4j
class TestRequestContext extends AbstractRequestContext {
    OutputStream  _responseBody
    def _wasClosed = false
    Closure _callBack


    def TestRequestContext(String method, Map headers, String path,  byte[] data, Closure callBack) {

        this._callBack = callBack
        this.requestBody = new ByteArrayInputStream(data ?: new byte[0] )
        this.responseBody = _responseBody = new ByteArrayOutputStream()
        setAttribute(GServ.contextAttributes.isWrapper, true)
        this.requestURI = new URI(path)
        this.requestMethod = method
        this.requestHeaders = headers as Map
    }

    @Override
    def close() {
        if(_wasClosed){
            log.warn("RequestContext called multiple times.")
        }
        _wasClosed = true
        _callBack(responseCode, responseHeaders, _responseBody.toByteArray())
    }

    @Override
    def setStreams(InputStream is, OutputStream os) {
        this.requestBody=is
        this.responseBody = os
    }


    void sendResponseHeaders( int responseCode, long size){
        this.responseCode = responseCode
        /// really can't use the size - yet
    }

}
