package ca.uhn.fhir.model.base.composite;

import org.hl7.fhir.instance.model.api.INarrative;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;

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
 * @param <T> The narrative status enum type  
 */
public abstract class BaseNarrativeDt<T extends Enum<?>> extends BaseIdentifiableElement implements ICompositeDatatype, INarrative {

	private static final long serialVersionUID = -525238683230100077L;

	public abstract BoundCodeDt<T> getStatus();

	@Override
	public void setDivAsString(String theString)  {
		getDiv().setValueAsString(theString);
	}

	@Override
	public String getDivAsString() {
		return getDiv().getValueAsString();
	}

	@Override
	public INarrative setStatusAsString(String theString) {
		getStatus().setValueAsString(theString);
		return this;
	}

	@Override
	public String getStatusAsString() {
		return getStatus().getValueAsString();
	}

	public abstract XhtmlDt getDiv();
	
}
