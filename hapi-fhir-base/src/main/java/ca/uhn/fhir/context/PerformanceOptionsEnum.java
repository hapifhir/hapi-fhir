package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

/**
 * This enum contains options to be used for {@link FhirContext#setPerformanceOptions(PerformanceOptionsEnum...)}
 */
public enum PerformanceOptionsEnum {

	/**
	 * When this option is set, model classes will not be scanned for children until the 
	 * child list for the given type is actually accessed.
	 * <p>
	 * The effect of this option is that reflection operations to scan children will be
	 * deferred, and some may never happen if specific model types aren't actually used.
	 * This option is useful on environments where reflection is particularly slow, e.g.
	 * Android or low powered devices.
	 * </p> 
	 */
	DEFERRED_MODEL_SCANNING
	
}
