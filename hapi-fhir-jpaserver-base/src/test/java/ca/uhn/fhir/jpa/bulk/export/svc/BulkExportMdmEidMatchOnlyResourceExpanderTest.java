/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyExpandSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by claude-opus-4-8
@ExtendWith(MockitoExtension.class)
class BulkExportMdmEidMatchOnlyResourceExpanderTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4();

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private MdmEidMatchOnlyExpandSvc myMdmEidMatchOnlyExpandSvc;

	@Mock
	private IIdHelperService<JpaPid> myIdHelperService;

	@SuppressWarnings("rawtypes")
	@Mock
	private IFhirResourceDao myGroupDao;

	private BulkExportMdmEidMatchOnlyResourceExpander myExpander;

	@BeforeEach
	void beforeEach() {
		myExpander = new BulkExportMdmEidMatchOnlyResourceExpander(
				myDaoRegistry, myMdmEidMatchOnlyExpandSvc, ourFhirContext, myIdHelperService);
	}

	private MdmSettings settingsWith(boolean theSearchAllPartitionForMatch) {
		MdmSettings settings = new MdmSettings(null);
		settings.setSearchAllPartitionForMatch(theSearchAllPartitionForMatch);
		return settings;
	}

	@SuppressWarnings("unchecked")
	private void setupGroupRead() {
		Group group = new Group();
		group.addMember().getEntity().setReference("Patient/bulk-p1");
		when(myDaoRegistry.getResourceDao("Group")).thenReturn(myGroupDao);
		when(myGroupDao.read(any(IIdType.class), any(SystemRequestDetails.class))).thenReturn(group);
		when(myMdmEidMatchOnlyExpandSvc.expandMdmBySourceResourceId(any(), any()))
				.thenReturn(Set.of("Patient/bulk-p1"));
		when(myIdHelperService.resolveResourcePids(
						any(RequestPartitionId.class), anyList(), any(ResolveIdentityMode.class)))
				.thenReturn(List.of(JpaPid.fromId(1L)));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandGroup_withSearchAllPartitionForMatch_widensMemberExpandAndResolveOnlyWhenEnabled(
			boolean theSearchAllPartitionForMatch) {
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(0);
		setupGroupRead();
		myExpander.setMdmSettings(settingsWith(theSearchAllPartitionForMatch));

		myExpander.expandGroup("Group/g1", requestPartitionId);

		RequestPartitionId expectedPartition =
				theSearchAllPartitionForMatch ? RequestPartitionId.allPartitions() : requestPartitionId;

		ArgumentCaptor<RequestPartitionId> expandPartitionCaptor = ArgumentCaptor.forClass(RequestPartitionId.class);
		verify(myMdmEidMatchOnlyExpandSvc).expandMdmBySourceResourceId(expandPartitionCaptor.capture(), any());
		assertThat(expandPartitionCaptor.getValue()).isEqualTo(expectedPartition);

		ArgumentCaptor<RequestPartitionId> resolvePartitionCaptor = ArgumentCaptor.forClass(RequestPartitionId.class);
		verify(myIdHelperService)
				.resolveResourcePids(resolvePartitionCaptor.capture(), anyList(), any(ResolveIdentityMode.class));
		assertThat(resolvePartitionCaptor.getValue()).isEqualTo(expectedPartition);

		ArgumentCaptor<SystemRequestDetails> srdCaptor = ArgumentCaptor.forClass(SystemRequestDetails.class);
		verify(myGroupDao).read(any(IIdType.class), srdCaptor.capture());
		assertThat(srdCaptor.getValue().getRequestPartitionId())
				.as("the Group read itself always stays on the request partition")
				.isEqualTo(requestPartitionId);
	}

	@Test
	void expandGroup_withNullSettings_usesRequestPartitionForMemberExpandAndResolve() {
		// Defensive fallback: settings should always be set before this expander runs, but if they are
		// somehow null, expansion must degrade to the request partition rather than throw.
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(0);
		setupGroupRead();

		myExpander.expandGroup("Group/g1", requestPartitionId);

		ArgumentCaptor<RequestPartitionId> expandPartitionCaptor = ArgumentCaptor.forClass(RequestPartitionId.class);
		verify(myMdmEidMatchOnlyExpandSvc).expandMdmBySourceResourceId(expandPartitionCaptor.capture(), any());
		assertThat(expandPartitionCaptor.getValue()).isEqualTo(requestPartitionId);

		ArgumentCaptor<RequestPartitionId> resolvePartitionCaptor = ArgumentCaptor.forClass(RequestPartitionId.class);
		verify(myIdHelperService)
				.resolveResourcePids(resolvePartitionCaptor.capture(), anyList(), any(ResolveIdentityMode.class));
		assertThat(resolvePartitionCaptor.getValue()).isEqualTo(requestPartitionId);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandPatient_withSearchAllPartitionForMatch_widensToAllPartitionsOnlyWhenEnabled(
			boolean theSearchAllPartitionForMatch) {
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(0);
		when(myMdmEidMatchOnlyExpandSvc.expandMdmBySourceResourceId(any(), any()))
				.thenReturn(Collections.emptySet());
		myExpander.setMdmSettings(settingsWith(theSearchAllPartitionForMatch));

		myExpander.expandPatient("Patient/bulk-p1", requestPartitionId);

		RequestPartitionId expectedPartition =
				theSearchAllPartitionForMatch ? RequestPartitionId.allPartitions() : requestPartitionId;
		ArgumentCaptor<RequestPartitionId> partitionCaptor = ArgumentCaptor.forClass(RequestPartitionId.class);
		verify(myMdmEidMatchOnlyExpandSvc).expandMdmBySourceResourceId(partitionCaptor.capture(), any());
		assertThat(partitionCaptor.getValue()).isEqualTo(expectedPartition);
	}
}
