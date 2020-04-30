package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.stream.Collectors;

public class SnapshotGeneratorImpl {
	public static SnapshotGenerator forR5(IFhirResourceDao sdDao) {
		return new SnapshotGenerator() {
			private IFhirResourceDaoStructureDefinition dao = (IFhirResourceDaoStructureDefinition) sdDao;
			@Override
			public Collection<IBaseResource> generateFrom(Collection<IBaseResource> structureDefinitions) {
				return structureDefinitions.stream()
					.map(org.hl7.fhir.r5.model.StructureDefinition.class::cast)
					.map(sd -> sd.hasSnapshot() ? sd : dao.generateSnapshot(sd, null, null, null))
					.collect(Collectors.toList());
			}
		};
	}

	public static SnapshotGenerator forR4(IFhirResourceDao sdDao) {
		return new SnapshotGenerator() {
			private IFhirResourceDaoStructureDefinition dao = (IFhirResourceDaoStructureDefinition) sdDao;
			@Override
			public Collection<IBaseResource> generateFrom(Collection<IBaseResource> structureDefinitions) {
				return structureDefinitions.stream()
					.map(org.hl7.fhir.r4.model.StructureDefinition.class::cast)
					.map(sd -> sd.hasSnapshot() ? sd : dao.generateSnapshot(sd, null, null, null))
					.collect(Collectors.toList());
			}
		};
	}

	public static SnapshotGenerator forDstu3(IFhirResourceDao sdDao) {
		return new SnapshotGenerator() {
			private IFhirResourceDaoStructureDefinition dao = (IFhirResourceDaoStructureDefinition) sdDao;
			@Override
			public Collection<IBaseResource> generateFrom(Collection<IBaseResource> structureDefinitions) {
				return structureDefinitions.stream()
					.map(org.hl7.fhir.dstu3.model.StructureDefinition.class::cast)
					.map(sd -> sd.hasSnapshot() ? sd : dao.generateSnapshot(sd, null, null, null))
					.collect(Collectors.toList());
			}
		};
	}
}
