package ca.uhn.fhir.jpa.model.pkspike.embeddedid;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.EntityFixture;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import ca.uhn.fhir.jpa.model.pkspike.SchemaCleanerExtension;
import ca.uhn.fhir.jpa.model.pkspike.idclass.ResJoinIdClassEntity;
import ca.uhn.fhir.jpa.model.pkspike.idclass.ResRootIdClassEntity;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	EmbeddedIdTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class EmbeddedIdPkJpaBindingTest {


	@Nested
	class Common extends BasicEntityTestTemplate<ResRootEmbeddedIdEntity, ResJoinEmbeddedIdEntity> {
		Common() {
			super(EntityFixture.build(ResRootEmbeddedIdEntity.class, ResJoinEmbeddedIdEntity.class));
		}
	}

}
