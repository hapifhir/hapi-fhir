package ca.uhn.fhir.jpa.dao.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.uhn.fhir.jpa.entity.SearchResult;

public interface ISearchResultDao  extends JpaRepository<SearchResult, Long> {
	// nothing
}
