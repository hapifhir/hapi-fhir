package ca.uhn.fhir.jpa.config.util;

import ca.uhn.hapi.fhir.sql.hibernatesvc.DatabasePartitionModeIdFilteringMappingContributor;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.spi.AdditionalMappingContributor;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.ValidationSettings;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.envers.Audited;
import org.hibernate.envers.boot.internal.EnversService;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class HapiEnversServiceTest {

	/**
	 * Hibernate runs mapping contributors in classpath order. When Envers runs before the partition id filter,
	 * it captures the entity ids with the partition id still in them.
	 */
	@Test
	void buildSessionFactory_enversContributorRunsBeforePartitionIdFilter_starts() {
		ClassLoaderServiceImpl enversFirst = new ClassLoaderServiceImpl(getClass().getClassLoader()) {
			@Override
			public <S> Collection<S> loadJavaServices(Class<S> theServiceContract) {
				Collection<S> services = super.loadJavaServices(theServiceContract);
				if (theServiceContract != AdditionalMappingContributor.class) {
					return services;
				}
				List<S> retVal = new ArrayList<>(services);
				retVal.sort(Comparator.comparing(t -> t instanceof DatabasePartitionModeIdFilteringMappingContributor));
				return retVal;
			}
		};
		BootstrapServiceRegistry bootstrapRegistry =
				new BootstrapServiceRegistryBuilder().applyClassLoaderService(enversFirst).build();
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder(bootstrapRegistry)
				.applySetting(JdbcSettings.ALLOW_METADATA_ON_BOOT, false)
				.applySetting(JdbcSettings.DIALECT, H2Dialect.class.getName())
				.applySetting(ValidationSettings.JAKARTA_VALIDATION_MODE, "none")
				.addService(HapiHibernateDialectSettingsService.class, new HapiHibernateDialectSettingsService());
		registryBuilder.addService(EnversService.class, new HapiEnversService(registryBuilder::getSettings));
		StandardServiceRegistry registry = registryBuilder.build();

		try (SessionFactory sessionFactory = new MetadataSources(registry)
				.addAnnotatedClass(PartitionedEntity.class)
				.addAnnotatedClass(AuditedEntity.class)
				.buildMetadata()
				.buildSessionFactory()) {
			assertThat(sessionFactory.getMetamodel().entity(PartitionedEntity.class)).isNotNull();
		}
	}

	@Entity
	@Table(name = "TEST_PARTITIONED")
	@IdClass(PartitionedEntityPk.class)
	static class PartitionedEntity {
		@Id
		@Column(name = "PID")
		private Long myId;

		@Id
		@PartitionedIdProperty
		@Column(name = "PARTITION_ID")
		private Integer myPartitionIdValue;
	}

	static class PartitionedEntityPk implements Serializable {
		private Long myId;
		private Integer myPartitionIdValue;

		@Override
		public boolean equals(Object theO) {
			return theO instanceof PartitionedEntityPk that
					&& Objects.equals(myId, that.myId)
					&& Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myId, myPartitionIdValue);
		}
	}

	@Entity
	@Table(name = "TEST_AUDITED")
	@Audited
	static class AuditedEntity {
		@Id
		@Column(name = "PID")
		private Long myId;
	}
}
