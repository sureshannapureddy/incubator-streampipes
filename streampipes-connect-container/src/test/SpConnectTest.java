/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.streampipes.rest.impl.connect;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.apache.streampipes.model.Response;
import org.apache.streampipes.model.SpDataSet;
import org.apache.streampipes.model.connect.adapter.AdapterDescription;
import org.apache.streampipes.model.connect.adapter.AdapterSetDescription;
import org.apache.streampipes.model.connect.adapter.AdapterStreamDescription;
import org.apache.streampipes.model.schema.EventSchema;
import org.apache.streampipes.rest.Mock;
import org.apache.streampipes.rest.TestUtil;
import org.apache.streampipes.storage.couchdb.impl.AdapterStorageImpl;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SpConnectTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(Mock.PORT);

    @Before
    public  void before() {
    }

    @Test
    public void isStreamAdapterSuccess() {
        AdapterStorageImpl adapterStorage = mock(AdapterStorageImpl.class);
        org.mockito.Mockito.when(adapterStorage.getAdapter(org.mockito.ArgumentMatchers.any(String.class)))
                .thenReturn(new AdapterStreamDescription());


        assertTrue(SpConnect.isStreamAdapter("", adapterStorage));
    }

    @Test
    public void isStreamAdapterFail() {
        AdapterStorageImpl adapterStorage = mock(AdapterStorageImpl.class);
        org.mockito.Mockito.when(adapterStorage.getAdapter(org.mockito.ArgumentMatchers.any(String.class)))
                .thenReturn(new AdapterSetDescription());

        assertFalse(SpConnect.isStreamAdapter("", adapterStorage));
    }


    @Test
    public void getAdapterStreamDescriptionWithoutType() {
        AdapterDescription asd = SpConnect
                .getAdapterDescription(TestUtil.getMinimalStreamAdapterJsonLD());

        assertTrue(asd instanceof AdapterStreamDescription);
    }

    @Test
    public void getAdapterSetDescriptionWithoutType() {
        AdapterDescription asd = SpConnect.
                getAdapterDescription(TestUtil.getMinimalSetAdapterJsonLD());

        assertTrue(asd instanceof AdapterSetDescription);
    }

    @Test
    public void startStreamAdapterTest() {
        // expected http request to connect-container /invoke/stream
        Response expected = new Response("id",true);
        stubFor(post(urlEqualTo("/api/v1/invoke/stream"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(expected.toString())));

        AdapterStreamDescription adapter = new AdapterStreamDescription();
        adapter.setUri("http://test.de/1");

        String result = SpConnect.startStreamAdapter(adapter, Mock.HOST + "/");

        assertEquals(SpConnectUtils.SUCCESS, result);
        verify(postRequestedFor(urlEqualTo("/api/v1/invoke/stream"))
                .withHeader("Content-Type", equalTo("application/json; charset=UTF-8")));
    }

    @Test
    public void invokeAdapterTest() {
        Response expected = new Response("id",true);
        stubFor(post(urlEqualTo("/api/v1/invoke/set"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(expected.toString())));

        AdapterSetDescription adapterSetDescription = new AdapterSetDescription();
        adapterSetDescription.setUri("http://test.adapter");
        AdapterStorageImpl adapterStorage = mock(AdapterStorageImpl.class);
        org.mockito.Mockito.when(adapterStorage.getAdapter(org.mockito.ArgumentMatchers.any(String.class)))
                .thenReturn(adapterSetDescription);


        SpDataSet dataSet = new SpDataSet("http://one.de", "name", "desc", new EventSchema());

        SpConnect spConnect = new SpConnect();
        String result = spConnect.invokeAdapter("1234", dataSet, Mock.HOST + "/", adapterStorage);

        assertEquals(SpConnectUtils.SUCCESS, result);
        verify(postRequestedFor(urlEqualTo("/api/v1/invoke/set"))
                .withHeader("Content-Type", equalTo("application/json; charset=UTF-8")));

    }

    @Test
    public void stopStreamAdapterTest() {
        Response expected = new Response("id",true);
        stubFor(post(urlEqualTo("/api/v1/stopAdapter/stream"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(expected.toString())));

        AdapterDescription adapterDescription = new AdapterSetDescription();
        adapterDescription.setUri("http://test.adapter");
        AdapterStorageImpl adapterStorage = mock(AdapterStorageImpl.class);
        org.mockito.Mockito.when(adapterStorage.getAdapter(org.mockito.ArgumentMatchers.any(String.class)))
                .thenReturn(adapterDescription);


        SpConnect spConnect = new SpConnect();
        String result = spConnect.stopStreamAdapter("1234",Mock.HOST + "/", adapterStorage);

        assertEquals(SpConnectUtils.SUCCESS, result);
        verify(postRequestedFor(urlEqualTo("/api/v1/stopAdapter/stream"))
                .withHeader("Content-Type", equalTo("application/json; charset=UTF-8")));
    }

    @Test
    public void stopSetAdapterTest() {
        Response expected = new Response("id",true);
        stubFor(post(urlEqualTo("/api/v1/stopAdapter/set"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(expected.toString())));

        AdapterDescription adapterDescription = new AdapterSetDescription();
        adapterDescription.setUri("http://test.adapter");
        AdapterStorageImpl adapterStorage = mock(AdapterStorageImpl.class);
        org.mockito.Mockito.when(adapterStorage.getAdapter(org.mockito.ArgumentMatchers.any(String.class)))
                .thenReturn(adapterDescription);


        SpConnect spConnect = new SpConnect();
        String result = spConnect.stopSetAdapter("1234",Mock.HOST + "/", adapterStorage);

        assertEquals(SpConnectUtils.SUCCESS, result);
        verify(postRequestedFor(urlEqualTo("/api/v1/stopAdapter/set"))
                .withHeader("Content-Type", equalTo("application/json; charset=UTF-8")));
    }

//    @Test
//    public void installDatasource() {
////        stubFor(post(urlEqualTo("/api/v1/stopAdapter/set"))
////                .willReturn(aResponse()
////                        .withStatus(200)
////                        .wit
////                        .withBody(expected.toString())));
//
//        SpConnect spConnect = new SpConnect();
//        boolean result = spConnect.installDatasource();
//
//        assertEquals(true, result);
//    }
}