/*
 * #%L
 * HAPI FHIR - OSGi Server Framework Bundle
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
package ca.uhn.fhir.mongodb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonDocumentWriter;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.junit.Test;

import com.mongodb.MongoClientSettings;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mongodb.codec.FhirResourceCodec;
import ca.uhn.fhir.mongodb.codec.FhirResourceCodecProvider;

/**
 *
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public class BasicBsonCodecTests {

	@Test
	public void testRegistry() {
		
		FhirContext fhirContext = FhirContext.forDstu3();
		
		CodecRegistry codecRegistry = 
				CodecRegistries.fromRegistries(
						MongoClientSettings.getDefaultCodecRegistry(),
						CodecRegistries.fromProviders(
								new FhirResourceCodecProvider(fhirContext)
						)
				 );
		Codec<String> strCodec = codecRegistry.get(String.class);
		assertEquals(strCodec.getEncoderClass(), String.class);
		Codec<BsonString> bstrCodec = codecRegistry.get(BsonString.class);
		assertEquals(bstrCodec.getEncoderClass(), BsonString.class);
		Codec<Patient> pCodec = codecRegistry.get(Patient.class);
		assertTrue(pCodec instanceof FhirResourceCodec);
		assertEquals(pCodec.getEncoderClass(), Patient.class);
	}

	@Test
	public void testEncoderDecoder() {
		FhirContext fhirContext = FhirContext.forDstu3();
		String refVal = "http://my.org/FooBar";
		Patient fhirPat = new Patient();
		fhirPat.addExtension().setUrl("x1").setValue(new Reference(refVal));
		
		CodecRegistry codecRegistry = 
				CodecRegistries.fromRegistries(
						MongoClientSettings.getDefaultCodecRegistry(),
						CodecRegistries.fromProviders(
								new FhirResourceCodecProvider(fhirContext)
						)
				 );
		Codec<Patient> pCodec = codecRegistry.get(Patient.class);
		
		BsonDocument bsonPatient = new BsonDocument();
		BsonWriter bsonWriter = new BsonDocumentWriter(bsonPatient);
		pCodec.encode(bsonWriter, fhirPat, EncoderContext.builder().build());
		assertTrue(bsonPatient.size() > 0);
		System.out.println("Encoded Patient:\n"+bsonPatient.toString());
		
		BsonReader bsonReader = bsonPatient.asBsonReader();
		Patient newPat = pCodec.decode(bsonReader, DecoderContext.builder().build());
		List<Extension> extensions = newPat.getExtensionsByUrl("x1");
		assertEquals(1, extensions.size());
		assertEquals(refVal, ((Reference) extensions.get(0).getValue()).getReference());

	}

}
