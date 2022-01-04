/**
 * Module to support Subscriptions
 * <p>
 * Subscriptions are partition aware
 * <p>
 * The functionalities of this module follows the HL7 spec on Subscriptions:
 * http://hl7.org/fhir/subscription.html
 * <p>
 * Activated by {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setPartitioningEnabled(boolean)}
 */
package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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
