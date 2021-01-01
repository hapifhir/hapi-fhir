package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

/**
 * Use this API with caution, it may change!
 */
public class ClasspathUtil {

	private static final Logger ourLog = LoggerFactory.getLogger(ClasspathUtil.class);

	public static String loadResource(String theClasspath) {
		Function<InputStream, InputStream> streamTransform = t -> t;
		return loadResource(theClasspath, streamTransform);
	}

	/**
	 * Load a classpath resource, throw an {@link InternalErrorException} if not found
	 */
	@Nonnull
	public static InputStream loadResourceAsStream(String theClasspath) {
		InputStream retVal = ClasspathUtil.class.getResourceAsStream(theClasspath);
		if (retVal == null) {
			throw new InternalErrorException("Unable to find classpath resource: " + theClasspath);
		}
		return retVal;
	}

	/**
	 * Load a classpath resource, throw an {@link InternalErrorException} if not found
	 */
	@Nonnull
	public static String loadResource(String theClasspath, Function<InputStream, InputStream> theStreamTransform) {
		InputStream stream = ClasspathUtil.class.getResourceAsStream(theClasspath);
		try {
			if (stream == null) {
				throw new IOException("Unable to find classpath resource: " + theClasspath);
			}
			try {
				InputStream newStream = theStreamTransform.apply(stream);
				return IOUtils.toString(newStream, Charsets.UTF_8);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
	}

	@Nonnull
	public static String loadCompressedResource(String theClasspath) {
		Function<InputStream, InputStream> streamTransform = t -> {
			try {
				return new GZIPInputStream(t);
			} catch (IOException e) {
				throw new InternalErrorException(e);
			}
		};
		return loadResource(theClasspath, streamTransform);
	}

	@Nonnull
	public static <T extends IBaseResource> T loadResource(FhirContext theCtx, Class<T> theType, String theClasspath) {
		String raw = loadResource(theClasspath);
		return EncodingEnum.detectEncodingNoDefault(raw).newParser(theCtx).parseResource(theType, raw);
	}

	public static void close(InputStream theInput) {
		try {
			if (theInput != null) {
				theInput.close();
			}
		} catch (IOException e) {
			ourLog.debug("Closing InputStream threw exception", e);
		}
	}

	public static Function<InputStream, InputStream> withBom() {
		return t -> new BOMInputStream(t);
	}

	public static byte[] loadResourceAsByteArray(String theClasspath) {
		InputStream stream = loadResourceAsStream(theClasspath);
		try {
			return IOUtils.toByteArray(stream);
		} catch (IOException e) {
			throw new InternalErrorException(e);
		} finally {
			close(stream);
		}
	}
}
