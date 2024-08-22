package ca.uhn.fhir.jpa.model.pkspike.partitionkey;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate;
import ca.uhn.fhir.jpa.model.pkspike.EntityFixture;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import ca.uhn.fhir.jpa.model.pkspike.SchemaCleanerExtension;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	PartitionTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
class PartitionJpaBindingTest {
	@Nested
	class Common extends BasicEntityTestTemplate {
		Common() {
			super(EntityFixture.build(ResRootPartitionEntity.class, ResJoinPartitionEntity.class));
		}
	}

}
