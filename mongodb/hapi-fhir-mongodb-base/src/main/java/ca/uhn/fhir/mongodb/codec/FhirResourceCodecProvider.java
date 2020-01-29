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

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;

/**
 * Bson Codec provider for the Fhir Resource Bson Codec.
 * <p>
 * Creates Bson Codec instances for serializing/deserializing
 * FHIR Resources to MongoDB documents. Here is an example of
 * how this is used:
 * <pre>
 * CodecRegistry MONGO_FHIR_CODEC_REGISTRY = CodecRegistries.fromRegistries(
 *                                                MongoClientSettings.getDefaultCodecRegistry(),
 *                                                CodecRegistries.fromProviders(
 *                                                     new FhirResourceCodecProvider(fhirContext)
 *                                                )
 *                                           );
 * </pre>
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public class FhirResourceCodecProvider implements CodecProvider {
	
	private static Logger log = LoggerFactory.getLogger(FhirResourceCodecProvider.class);
	private FhirContext fhirContext;
	private Map<Class<?>,Codec<?>> codecMap = new LinkedHashMap<Class<?>,Codec<?>>();

	public FhirResourceCodecProvider () {
		super();
	}
	public FhirResourceCodecProvider (FhirContext fhirContext) {
		this();
		setFhirContext(fhirContext);
	}
	public void setFhirContext(FhirContext fhirContext) {
		this.fhirContext = fhirContext;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> Codec<T> get (Class<T> targetClass, CodecRegistry registry) {
		Codec<T> result = (Codec<T>)codecMap.get(targetClass);
		if (null == result) {
			if (IBaseResource.class.isAssignableFrom(targetClass)) {
				log.trace("Create new Bson codec for FHIR resource ["+targetClass.getSimpleName()+"]");
				result = new FhirResourceCodec(fhirContext, targetClass, registry);
			}
			if (result != null) {
				codecMap.put(targetClass, result);
			}
		}
		return result;
	}

}
