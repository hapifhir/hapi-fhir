/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.config;

import ca.uhn.fhir.cr.config.CrConfigCondition;
import ca.uhn.fhir.cr.config.RepositoryConfig;
import ca.uhn.fhir.cr.config.r4.ApplyOperationConfig;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This class exists as a wrapper for the CR configs required for CDS on FHIR to be loaded only when dependencies are met.
 * Adding the condition to the configs themselves causes issues with downstream projects.
 *
 */
@Configuration
@Conditional(CrConfigCondition.class)
@Import({RepositoryConfig.class, ApplyOperationConfig.class})
public class CdsCrConfig {}
