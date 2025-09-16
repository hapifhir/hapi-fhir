package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.util.List;

public record TransactionPrePartitionResponse(List<IBaseBundle> splitBundles) {}
