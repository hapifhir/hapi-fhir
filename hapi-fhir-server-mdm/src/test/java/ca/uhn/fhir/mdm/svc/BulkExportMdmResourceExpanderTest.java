/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// Created by Claude 4.5 Sonnet
@ExtendWith(MockitoExtension.class)
class BulkExportMdmResourceExpanderTest {

	@Mock
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;

	@Mock
	private IMdmLinkDao myMdmLinkDao;

	@Mock
	private IIdHelperService<JpaPid> myIdHelperService;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDao<Patient> myPatientDao;

	@Mock
	private FhirContext myFhirContext;

	@InjectMocks
	private BulkExportMdmResourceExpander myExpander;

	@BeforeEach
	void setUp() {
		// Common setup for DAO registry
		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
	}

	@Test
	void testExpandPatient_withMdmLinks_returnsExpandedCluster() {

		String inputPatientId = "Patient/123";
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		// Mock patient resource
		Patient patient = new Patient();
		patient.setId("123");
		when(myPatientDao.read(any(IdDt.class), any(SystemRequestDetails.class)))
				.thenReturn(patient);

		// Mock PID resolution
		JpaPid patientPid = JpaPid.fromId(123L);
		when(myIdHelperService.getPidOrNull(eq(partitionId), eq(patient)))
				.thenReturn(patientPid);

		// Mock MDM links - patient 123 is linked to golden 999 along with patients 456 and 789
		JpaPid goldenPid = JpaPid.fromId(999L);
		JpaPid sourcePid1 = JpaPid.fromId(123L); // Original patient
		JpaPid sourcePid2 = JpaPid.fromId(456L); // Linked patient 1
		JpaPid sourcePid3 = JpaPid.fromId(789L); // Linked patient 2

		MdmPidTuple<JpaPid> tuple1 = MdmPidTuple.fromGoldenAndSource(goldenPid, sourcePid1);
		MdmPidTuple<JpaPid> tuple2 = MdmPidTuple.fromGoldenAndSource(goldenPid, sourcePid2);
		MdmPidTuple<JpaPid> tuple3 = MdmPidTuple.fromGoldenAndSource(goldenPid, sourcePid3);

		when(myMdmLinkDao.expandPidsBySourcePidAndMatchResult(eq(patientPid), eq(MdmMatchResultEnum.MATCH)))
				.thenReturn(List.of(tuple1, tuple2, tuple3));

		// Mock translatePidsToForcedIds for cache population
		Map<JpaPid, Optional<String>> pidToForcedIdMap = new HashMap<>();
		pidToForcedIdMap.put(sourcePid1, Optional.of("123"));
		pidToForcedIdMap.put(sourcePid2, Optional.of("456"));
		pidToForcedIdMap.put(sourcePid3, Optional.of("789"));
		PersistentIdToForcedIdMap<JpaPid> persistentIdMap = new PersistentIdToForcedIdMap<>(pidToForcedIdMap);
		when(myIdHelperService.translatePidsToForcedIds(any())).thenReturn(persistentIdMap);

		// Mock PID to forced ID translation
		when(myIdHelperService.translatePidIdToForcedIdWithCache(goldenPid))
				.thenReturn(Optional.of("golden"));
		when(myIdHelperService.translatePidIdToForcedIdWithCache(sourcePid1))
				.thenReturn(Optional.of("123"));
		when(myIdHelperService.translatePidIdToForcedIdWithCache(sourcePid2))
				.thenReturn(Optional.of("456"));
		when(myIdHelperService.translatePidIdToForcedIdWithCache(sourcePid3))
				.thenReturn(Optional.of("789"));

		// Mock cache service (not already populated)
		when(myMdmExpansionCacheSvc.hasBeenPopulated()).thenReturn(false);

		// When
		Set<String> result = myExpander.expandPatient(inputPatientId, partitionId);

		// Then - Should return all 4 patients (golden + 3 sources)
		assertThat(result).containsExactlyInAnyOrder(
				"Patient/golden",
				"Patient/123",
				"Patient/456",
				"Patient/789"
		);
		assertThat(result).hasSize(4);

		// Verify MDM link DAO was called
		verify(myMdmLinkDao, times(1)).expandPidsBySourcePidAndMatchResult(
				eq(patientPid),
				eq(MdmMatchResultEnum.MATCH)
		);

		// Verify cache was populated
		verify(myMdmExpansionCacheSvc, times(1)).setCacheContents(any());
	}

	@Test
	void testExpandPatient_withoutMdmLinks_returnsOnlySelf() {
		// Given - Patient with NO MDM links
		String inputPatientId = "Patient/123";
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		// Mock patient resource
		Patient patient = new Patient();
		patient.setId("123");
		when(myPatientDao.read(any(IdDt.class), any(SystemRequestDetails.class)))
				.thenReturn(patient);

		// Mock PID resolution
		JpaPid patientPid = JpaPid.fromId(123L);
		when(myIdHelperService.getPidOrNull(eq(partitionId), eq(patient)))
				.thenReturn(patientPid);

		// Mock MDM links - EMPTY list (no links)
		when(myMdmLinkDao.expandPidsBySourcePidAndMatchResult(eq(patientPid), eq(MdmMatchResultEnum.MATCH)))
				.thenReturn(List.of());

		// Mock PID to forced ID translation
		when(myIdHelperService.translatePidIdToForcedIdWithCache(patientPid))
				.thenReturn(Optional.of("123"));

		// When
		Set<String> result = myExpander.expandPatient(inputPatientId, partitionId);

		// Then - Should return only the original patient
		assertThat(result).containsExactly("Patient/123");
		assertThat(result).hasSize(1);

		// Verify MDM link DAO was called
		verify(myMdmLinkDao, times(1)).expandPidsBySourcePidAndMatchResult(
				eq(patientPid),
				eq(MdmMatchResultEnum.MATCH)
		);

		// Verify cache was NOT populated (no links)
		verify(myMdmExpansionCacheSvc, never()).setCacheContents(any());
	}

	@Test
	void testExpandPatient_withNumericPids_usesNumericIds() {
		// Given - Patient with MDM links but NO forced IDs
		String inputPatientId = "Patient/123";
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		// Mock patient resource
		Patient patient = new Patient();
		patient.setId("123");
		when(myPatientDao.read(any(IdDt.class), any(SystemRequestDetails.class)))
				.thenReturn(patient);

		// Mock PID resolution
		JpaPid patientPid = JpaPid.fromId(123L);
		when(myIdHelperService.getPidOrNull(eq(partitionId), eq(patient)))
				.thenReturn(patientPid);

		// Mock MDM links
		JpaPid goldenPid = JpaPid.fromId(999L);
		JpaPid sourcePid1 = JpaPid.fromId(123L);

		MdmPidTuple<JpaPid> tuple1 = MdmPidTuple.fromGoldenAndSource(goldenPid, sourcePid1);

		when(myMdmLinkDao.expandPidsBySourcePidAndMatchResult(eq(patientPid), eq(MdmMatchResultEnum.MATCH)))
				.thenReturn(List.of(tuple1));

		// Mock translatePidsToForcedIds for cache population - with EMPTY optionals (no forced IDs)
		Map<JpaPid, Optional<String>> pidToForcedIdMap = new HashMap<>();
		pidToForcedIdMap.put(sourcePid1, Optional.empty());
		PersistentIdToForcedIdMap<JpaPid> persistentIdMap = new PersistentIdToForcedIdMap<>(pidToForcedIdMap);
		when(myIdHelperService.translatePidsToForcedIds(any())).thenReturn(persistentIdMap);

		// Mock PID to forced ID translation - return EMPTY (no forced IDs)
		when(myIdHelperService.translatePidIdToForcedIdWithCache(any()))
				.thenReturn(Optional.empty());

		// Mock cache service
		when(myMdmExpansionCacheSvc.hasBeenPopulated()).thenReturn(false);

		// When
		Set<String> result = myExpander.expandPatient(inputPatientId, partitionId);

		// Then - Should return numeric IDs
		assertThat(result).containsExactlyInAnyOrder(
				"Patient/999",  // Numeric golden PID
				"Patient/123"   // Numeric source PID
		);
		assertThat(result).hasSize(2);
	}

	@Test
	void testExpandPatient_partitionContextPropagated() {
		// Given - Specific partition
		String inputPatientId = "Patient/123";
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(5);

		// Mock patient resource
		Patient patient = new Patient();
		patient.setId("123");
		when(myPatientDao.read(any(IdDt.class), any(SystemRequestDetails.class)))
				.thenReturn(patient);

		// Mock PID resolution
		JpaPid patientPid = JpaPid.fromId(123L);
		when(myIdHelperService.getPidOrNull(eq(partitionId), eq(patient)))
				.thenReturn(patientPid);

		// Mock MDM links (empty for simplicity)
		when(myMdmLinkDao.expandPidsBySourcePidAndMatchResult(eq(patientPid), eq(MdmMatchResultEnum.MATCH)))
				.thenReturn(List.of());

		// Mock PID to forced ID translation
		when(myIdHelperService.translatePidIdToForcedIdWithCache(patientPid))
				.thenReturn(Optional.of("123"));

		// When
		myExpander.expandPatient(inputPatientId, partitionId);

		// Then - Verify partition was used
		ArgumentCaptor<SystemRequestDetails> requestCaptor =
			ArgumentCaptor.forClass(SystemRequestDetails.class);
		verify(myPatientDao).read(any(IdDt.class), requestCaptor.capture());

		SystemRequestDetails capturedRequest = requestCaptor.getValue();
		assertThat(capturedRequest.getRequestPartitionId()).isEqualTo(partitionId);

		// Verify PID helper used correct partition
		verify(myIdHelperService).getPidOrNull(eq(partitionId), eq(patient));
	}
}
