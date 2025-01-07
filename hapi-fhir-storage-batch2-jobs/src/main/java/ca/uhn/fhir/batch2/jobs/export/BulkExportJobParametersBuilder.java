/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.ArrayUtil;
import ca.uhn.fhir.util.DatatypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a Builder class which helps with building
 * BulkExportJobParameters object
 */
public class BulkExportJobParametersBuilder {
	public static final String FARM_TO_TABLE_TYPE_FILTER_REGEX = "(?:,)(?=[A-Z][a-z]+\\?)";

	private Set<String> myResourceTypes;
	private Date mySince;
	private Date myUntil;
	private Set<String> myFilters;
	private String myOutputFormat;
	private BulkExportJobParameters.ExportStyle myExportStyle;
	private List<String> myPatientIds = new ArrayList<>();
	private String myGroupId;
	private boolean myExpandMdm;
	private RequestPartitionId myPartitionId;
	private String myExportIdentifier;
	private Set<String> myPostFetchFilterUrls;

	public BulkExportJobParametersBuilder resourceTypes(IPrimitiveType<String> theResourceTypes) {
		myResourceTypes = theResourceTypes == null
				? null
				: ArrayUtil.commaSeparatedListToCleanSet(theResourceTypes.getValueAsString());
		return this;
	}

	public BulkExportJobParametersBuilder since(IPrimitiveType<Date> theSince) {
		mySince = DatatypeUtil.toDateValue(theSince);
		return this;
	}

	public BulkExportJobParametersBuilder until(IPrimitiveType<Date> theUntil) {
		myUntil = DatatypeUtil.toDateValue(theUntil);
		return this;
	}

	public BulkExportJobParametersBuilder filters(List<IPrimitiveType<String>> theFilters) {
		myFilters = parseFilters(theFilters);
		return this;
	}

	public BulkExportJobParametersBuilder outputFormat(IPrimitiveType<String> theOutputFormat) {
		myOutputFormat = theOutputFormat != null ? theOutputFormat.getValueAsString() : Constants.CT_FHIR_NDJSON;
		return this;
	}

	public BulkExportJobParametersBuilder exportStyle(BulkExportJobParameters.ExportStyle theExportStyle) {
		myExportStyle = theExportStyle;
		return this;
	}

	public BulkExportJobParametersBuilder patientIds(List<IPrimitiveType<String>> thePatientIds) {
		myPatientIds = thePatientIds == null
				? null
				: thePatientIds.stream().map(IPrimitiveType::getValueAsString).collect(Collectors.toList());
		return this;
	}

	public BulkExportJobParametersBuilder groupId(IIdType theGroupId) {
		myGroupId = DatatypeUtil.toStringValue(theGroupId);
		return this;
	}

	public BulkExportJobParametersBuilder expandMdm(IPrimitiveType<Boolean> theExpandMdm) {
		final Boolean booleanValue = DatatypeUtil.toBooleanValue(theExpandMdm);
		myExpandMdm = booleanValue != null && booleanValue;
		return this;
	}

	public BulkExportJobParametersBuilder partitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
		return this;
	}

	public BulkExportJobParametersBuilder exportIdentifier(IPrimitiveType<String> theExportIdentifier) {
		myExportIdentifier = DatatypeUtil.toStringValue(theExportIdentifier);
		return this;
	}

	public BulkExportJobParametersBuilder postFetchFilterUrl(List<IPrimitiveType<String>> thePostFetchFilterUrl) {
		myPostFetchFilterUrls = parseFilters(thePostFetchFilterUrl);
		return this;
	}

	public BulkExportJobParameters build() {
		BulkExportJobParameters result = new BulkExportJobParameters();
		result.setExpandMdm(myExpandMdm);
		result.setExportIdentifier(myExportIdentifier);
		result.setExportStyle(myExportStyle);
		result.setFilters(myFilters);
		result.setGroupId(myGroupId);
		result.setOutputFormat(myOutputFormat);
		result.setPartitionId(myPartitionId);
		result.setPatientIds(myPatientIds);
		result.setResourceTypes(myResourceTypes);
		result.setSince(mySince);
		result.setUntil(myUntil);
		result.setPostFetchFilterUrls(myPostFetchFilterUrls);
		return result;
	}

	private Set<String> parseFilters(List<IPrimitiveType<String>> theFilters) {
		Set<String> retVal = null;
		if (theFilters != null) {
			retVal = new HashSet<>();
			for (IPrimitiveType<String> next : theFilters) {
				String typeFilterString = next.getValueAsString();
				Arrays.stream(typeFilterString.split(FARM_TO_TABLE_TYPE_FILTER_REGEX))
						.filter(StringUtils::isNotBlank)
						.forEach(retVal::add);
			}
		}
		return retVal;
	}
}
