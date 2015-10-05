package ca.uhn.fhir.jpa.dao.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.uhn.fhir.jpa.entity.ResourceTable;

public interface IResourceTableDao extends JpaRepository<ResourceTable, Long> {
	
}
