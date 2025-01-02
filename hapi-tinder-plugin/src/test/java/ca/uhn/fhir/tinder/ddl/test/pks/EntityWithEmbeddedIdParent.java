package ca.uhn.fhir.tinder.ddl.test.pks;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ENTITY_WITH_EMBEDDED_ID_PARENT")
public class EntityWithEmbeddedIdParent {

	@EmbeddedId
	private EntityWithEmbeddedIdPk myId;

	@Column(name = "NAME")
	private String myName;

}
