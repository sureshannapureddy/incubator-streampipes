/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.connect.rest.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.streampipes.connect.adapter.Adapter;
import org.streampipes.connect.exception.AdapterException;
import org.streampipes.connect.management.AdapterDeserializer;
import org.streampipes.connect.management.master.AdapterMasterManagement;
import org.streampipes.connect.management.master.GuessManagement;
import org.streampipes.connect.rest.AbstractContainerResource;
import org.streampipes.model.client.messages.Notifications;
import org.streampipes.model.connect.adapter.AdapterDescription;
import org.streampipes.model.connect.adapter.AdapterSetDescription;
import org.streampipes.model.connect.adapter.AdapterStreamDescription;
import org.streampipes.model.connect.guess.GuessSchema;
import org.streampipes.model.schema.EventPropertyPrimitive;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.rest.shared.annotation.JsonLdSerialized;
import org.streampipes.rest.shared.util.JsonLdUtils;
import org.streampipes.rest.shared.util.SpMediaType;
import org.streampipes.serializers.jsonld.JsonLdTransformer;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;


@Path("/api/v1/{username}/master/guess")
public class GuessResource extends AbstractContainerResource {

    private static final Logger logger = LoggerFactory.getLogger(GuessResource.class);

    private GuessManagement guessManagement;

    public GuessResource() {
        this.guessManagement = new GuessManagement();
    }

    public GuessResource(GuessManagement guessManagement) {
        this.guessManagement = guessManagement;
    }

//    @POST
//    @JsonLdSerialized
//    @Path("/schema")
//    @Produces(SpMediaType.JSONLD)
//    public Response guessSchema(String s, @PathParam("username") String userName) {
//        AdapterDescription adapterDescription = null;
//
//        try {
//            adapterDescription = AdapterDeserializer.getAdapterDescription(s);
//        } catch (AdapterException e) {
//            logger.error("Could not deserialize AdapterDescription: " + s, e);
//        }
//
//        GuessSchema result = guessManagement.guessSchema(adapterDescription);
//
//
//        return ok(result);
//    }

    @GET
    @Produces(SpMediaType.JSONLD)
    @Path("/format")
    public Response guessFormat() {
        //TODO
        return ok(true);
    }


    @GET
    @Produces(SpMediaType.JSONLD)
    @Path("/formatdescription")
    public Response guessFormatDescription() {
        //TODO
        return ok(true);
    }

    public void setGuessManagement(GuessManagement guessManagement) {
        this.guessManagement = guessManagement;
    }

    @POST
    @Produces(SpMediaType.JSONLD)
    @Path("/schema")
    public Response guessSchema(String ar, @PathParam("username") String userName) {

//        return ok(true);

//        JsonLdTransformer jsonLdTransformer = new JsonLdTransformer();
//
//        AdapterDescription a = null;
//        try {
//            if (ar.contains("AdapterSetDescription")){
//                a = jsonLdTransformer.fromJsonLd(ar, AdapterSetDescription.class);
//            } else {
//                a = jsonLdTransformer.fromJsonLd(ar, AdapterStreamDescription.class);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Adapter adapter = new Adapter("ipe-koi06.fzi.de:9092", "org.streampipes.streamconnect", true);
//        GuessSchema resultSchema = adapter.getSchema(a);

//         TODO get domainproperty probabilities


            EventSchema eventSchema = new EventSchema();
            EventPropertyPrimitive eventPropertyPrimitive = new EventPropertyPrimitive();
            eventPropertyPrimitive.setRuntimeType("http://schema.org/Number");
            eventPropertyPrimitive.setRuntimeName("id");


            eventSchema.setEventProperties(Arrays.asList(eventPropertyPrimitive));
            GuessSchema guessSchema = new GuessSchema();
            guessSchema.setEventSchema(eventSchema);
            return ok(guessSchema);
        }
//
//

}
