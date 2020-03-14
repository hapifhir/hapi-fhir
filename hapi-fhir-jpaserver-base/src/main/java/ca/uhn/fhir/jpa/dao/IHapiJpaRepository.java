package ca.uhn.fhir.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IHapiJpaRepository<T> extends JpaRepository<T, Long> {

	void deleteByPid(Long theId);

}
