package ca.uhn.fhir.batch2.jobs.imprt;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceOrderUtil {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceOrderUtil.class);

	private ResourceOrderUtil() {
		// non instantiable
	}

	/**
	 * This method attempts to order all resource type names so that if one resource
	 * has references to another, the latter comes before the former.
	 * <p>
	 * So for example, "Patient" would come before "Observation" in the list because
	 * Observation refers to Patient but not vice versa.
	 * <p>
	 * This implementation is a pretty naive bubble sort, and could probably be more
	 * efficient so don't call it repeatedly.
	 * <p>
	 * Note that the results can't cover every reference, since some are circular (e.g.
	 * MedicationStatement:part-of -> Observation:part-of -> MedicationStatement:part-of)
	 * So for those cases the order is arbitrary.
	 *
	 * @since 6.0.0
	 */
	public static List<String> getResourceOrder(FhirContext theFhirContext) {
		ArrayList<String> retVal = new ArrayList<>(theFhirContext.getResourceTypes());
		Set<String> moves = new HashSet<>();
		StopWatch sw = new StopWatch();

		int passCount = 0;
		for (; passCount < retVal.size(); passCount++) {
			ourLog.debug("Starting passCount {}", passCount);
			int changeCount = 0;

			for (int i = retVal.size() - 1; i >= 0; i--) {
				String typeAtCurrentIndex = retVal.get(i);

				RuntimeResourceDefinition typeDefinition = theFhirContext.getResourceDefinition(typeAtCurrentIndex);
				List<RuntimeSearchParam> params = typeDefinition
					.getSearchParams()
					.stream()
					.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
					.collect(Collectors.toList());
				for (RuntimeSearchParam nextParam : params) {
					for (String targetType : nextParam.getTargets()) {
						int targetIndex = retVal.indexOf(targetType);
						if (targetIndex > i) {

							String nextParamName = nextParam.getName();
							String key = typeAtCurrentIndex + " " + nextParamName + " " + targetType + " " + i + " " + targetIndex;
							if (!moves.add(key)) {
								continue;
							}

							ourLog.debug("Resource[{}] at index[{}] has SP[{}] with target[{}] at index[{}] - moving to index[{}]", typeAtCurrentIndex, i, nextParamName, targetType, targetIndex, i);

							retVal.set(targetIndex, typeAtCurrentIndex);
							retVal.set(i, targetType);

							i = targetIndex;

							changeCount++;
							break;
						}
					}
				}

			}

			ourLog.debug("Finished pass {} with {} changes", passCount, changeCount);
			if (changeCount == 0) {
				break;
			}
		}

		ourLog.info("Calculated optimal resource order in {} passes in {}", passCount, sw);

		return retVal;
	}


//	public static List<String> getResourceOrder(FhirContext theFhirContext) {
//		LinkedList<String> retVal = new LinkedList<>(theFhirContext.getResourceTypes());
//		Set<String> moves = new HashSet<>();
//		StopWatch sw = new StopWatch();
//
//		for (int rep = 0; rep < retVal.size(); rep++) {
//			ourLog.debug("Starting rep {}", rep);
//			int changeCount = 0;
//
//			for (int i = retVal.size() - 1; i >= 0; i--) {
//				String typeAtCurrentIndex = retVal.get(i);
//
//				RuntimeResourceDefinition typeDefinition = theFhirContext.getResourceDefinition(typeAtCurrentIndex);
//				List<RuntimeSearchParam> params = typeDefinition
//					.getSearchParams()
//					.stream()
//					.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
//					.collect(Collectors.toList());
//				for (RuntimeSearchParam nextParam : params) {
//					for (String targetType : nextParam.getTargets()) {
//						int targetIndex = retVal.indexOf(targetType);
//						if (targetIndex > i) {
//
//							String nextParamName = nextParam.getName();
//							String key = typeAtCurrentIndex + " " + nextParamName + " " + targetType + " " + i + " " + targetIndex;
//							if (!moves.add(key)) {
//								continue;
//							}
//
//							ourLog.debug("Resource[{}] at index[{}] has SP[{}] with target[{}] at index[{}] - moving to index[{}]", typeAtCurrentIndex, i, nextParamName, targetType, targetIndex, i);
//							retVal.remove(targetIndex);
//							retVal.add(i, targetType);
//							i++;
//							changeCount++;
//						}
//					}
//				}
//
//			}
//
//			ourLog.debug("Finished pass {} with {} changes", rep, changeCount);
//			if (changeCount == 0) {
//				break;
//			}
//		}
//
//		ourLog.info("Calculated optimal resource order in {}", sw);
//
//		return retVal;
//	}

}
