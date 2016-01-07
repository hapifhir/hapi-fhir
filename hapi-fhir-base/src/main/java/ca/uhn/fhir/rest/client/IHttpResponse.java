package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public interface IHttpResponse {

	/**
	 * @return the response status number 
	 */
	public int getStatus();

	/**
	 * @return the raw response underlying response object
	 */
	Object getResponse();

	/**
	 * @return the response mime type
	 */
	public String getMimeType();

	/**
	 * @return all the headers
	 */
	public Map<String, List<String>> getAllHeaders();

	/**
	 * @return the status info
	 */
	public String getStatusInfo();

	/**
	 * @return
	 * @throws IOException
	 */
	public Reader createReader() throws IOException;

	/**
	 * Read the entity as inputstream
	 * @return the inputstream
	 * @throws IOException
	 */
	public InputStream readEntity() throws IOException;

	/**
	 * Close the response
	 */
	public void close();

	
	  /**
     * Buffer the message entity data.
     * <p>
     * In case the message entity is backed by an unconsumed entity input stream,
     * all the bytes of the original entity input stream are read and stored in a
     * local buffer. The original entity input stream is consumed and automatically
     * closed as part of the operation and the method returns {@code true}.
     * </p>
     * <p>
     * In case the response entity instance is not backed by an unconsumed input stream
     * an invocation of {@code bufferEntity} method is ignored and the method returns
     * {@code false}.
     * </p>
     * <p>
     * This operation is idempotent, i.e. it can be invoked multiple times with
     * the same effect which also means that calling the {@code bufferEntity()}
     * method on an already buffered (and thus closed) message instance is legal
     * and has no further effect. Also, the result returned by the {@code bufferEntity()}
     * method is consistent across all invocations of the method on the same
     * {@code Response} instance.
     * </p>
     * <p>
     * Buffering the message entity data allows for multiple invocations of
     * {@code readEntity(...)} methods on the response instance. Note however, that
     * once the response instance itself is {@link #close() closed}, the implementations
     * are expected to release the buffered message entity data too. Therefore any subsequent
     * attempts to read a message entity stream on such closed response will result in an
     * {@link IllegalStateException} being thrown.
     * </p>
	 * @throws IOException 
     * @throws IllegalStateException in case the response has been {@link #close() closed}.
     */	
	void bufferEntitity() throws IOException;

}
