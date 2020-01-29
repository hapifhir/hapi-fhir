/*
 * #%L
 * HAPI FHIR - MongoDB Framework Bundle
 * %%
 * Copyright (C) 2016 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.mongodb.codec;

import java.io.IOException;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.parser.bson.BsonFhirStructure;
import ca.uhn.fhir.parser.bson.BsonFhirWriter;
import ca.uhn.fhir.parser.bson.TracingBsonWriter;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * BSON Codec serializing/deserializing FHIR Resources
 *  
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public class FhirResourceCodec<T extends IBaseResource> implements Codec<T> {
	private static Logger log = LoggerFactory.getLogger(FhirResourceCodec.class);

	private FhirContext fhirContext;
	private Class<T> targetClass;
	private CodecRegistry registry;

	public FhirResourceCodec (FhirContext fhirContext, Class<T> targetClass, CodecRegistry registry) {
		log.debug("New codec for resource class ["+targetClass.getName()+"]");
		this.fhirContext = fhirContext;
		this.targetClass = targetClass;
		this.registry = registry;
	}

	@Override
	public Class<T> getEncoderClass() {
		return targetClass;
	}

	
	@Override
	public void encode (BsonWriter writer, T resource, EncoderContext encoderContext) {
		BsonWriter bsonWriter = writer;
		if (log.isTraceEnabled()) {
			bsonWriter = new TracingBsonWriter(writer, log);
		}
    	log.trace("encode: call FHIR JSON parser for ["+resource.getClass().getName()+")");
		try {
			
			BsonFhirWriter bsonFhirWriter = new BsonFhirWriter(bsonWriter);
			IJsonLikeParser fhirParser =  (IJsonLikeParser)fhirContext.newJsonParser();
			fhirParser.encodeResourceToJsonLikeWriter(resource, bsonFhirWriter);
			
		} catch (DataFormatException | IOException e) {
			throw new InternalErrorException("Error encoding FHIR resource of type ["+targetClass.getName()+"] to MongoDB document", e);
		}
	}


	@Override
	public T decode (BsonReader reader, DecoderContext decoderContext) {
    	log.trace("decode: call FHIR JSON parser for ["+targetClass.getName()+")");
		try {

			BsonFhirStructure bsonFhirStructure = new BsonFhirStructure(reader);
			IJsonLikeParser fhirParser =  (IJsonLikeParser)fhirContext.newJsonParser();
			T resource = fhirParser.parseResource(targetClass, bsonFhirStructure);
			return resource;
		
		} catch (DataFormatException e) {
			throw new InternalErrorException("Error decoding FHIR resource of type ["+targetClass.getName()+"] from MongoDB document", e);
		}
	}

}
