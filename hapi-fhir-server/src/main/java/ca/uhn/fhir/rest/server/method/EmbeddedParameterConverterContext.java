/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.method;

import jakarta.annotation.Nullable;

import java.util.StringJoiner;

/**
 * Output of {@link EmbeddedParameterConverter} that captures either a NullParameter or a ParamInitializationContext.
 */
class EmbeddedParameterConverterContext {

	@Nullable
	private final NullParameter myNullParameter;

	@Nullable
	private final ParamInitializationContext myParamContext;

	public static EmbeddedParameterConverterContext forParameter(NullParameter theNullParameter) {
		return new EmbeddedParameterConverterContext(theNullParameter, null);
	}

	public static EmbeddedParameterConverterContext forEmbeddedContext(ParamInitializationContext theParamContext) {
		return new EmbeddedParameterConverterContext(null, theParamContext);
	}

	private EmbeddedParameterConverterContext(
			@Nullable NullParameter theNullParameter, @Nullable ParamInitializationContext theParamContext) {

		myNullParameter = theNullParameter;
		myParamContext = theParamContext;
	}

	@Nullable
	public NullParameter getParameter() {
		return myNullParameter;
	}

	@Nullable
	public ParamInitializationContext getParamContext() {
		return myParamContext;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", EmbeddedParameterConverterContext.class.getSimpleName() + "[", "]")
				.add("myNullParameter=" + myNullParameter)
				.add("myParamContext=" + myParamContext)
				.toString();
	}
}
