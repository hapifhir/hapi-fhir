package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class FhirResourceDaoBundleR4 extends BaseHapiFhirResourceDao<Bundle> {

	// nothing for now

}
