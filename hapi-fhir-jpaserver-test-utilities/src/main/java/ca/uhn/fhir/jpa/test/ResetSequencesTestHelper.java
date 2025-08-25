package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.hibernate.id.enhanced.PooledOptimizer;
import org.hibernate.mapping.Component;
import org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This JUnit extension resets the Hibernate sequence generator for the ResourceTable entity after each test,
 * meaning that the next resource will be assigned PID 1.
 */
@SuppressWarnings("SqlNoDataSourceInspection")
public class ResetSequencesTestHelper implements AfterEachCallback {

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		ApplicationContext appCtx = SpringExtension.getApplicationContext(theExtensionContext);
		EntityManager entityManager = appCtx.getBean(EntityManager.class);

		TransactionTemplate txTemplate = new TransactionTemplate(appCtx.getBean(PlatformTransactionManager.class));
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		txTemplate.afterPropertiesSet();

		/*
		 * Reset the database sequence
		 */

		txTemplate.executeWithoutResult(t -> {
			entityManager.createNativeQuery("drop sequence if exists SEQ_RESOURCE_ID").executeUpdate();
			entityManager.createNativeQuery("create sequence SEQ_RESOURCE_ID minvalue 1 start with 1 increment by 50").executeUpdate();
		});

		/*
		 * Drop the internal state from the sequence generator so that we don't use a cached value
		 * that was pulled from the sequence before it was reset.
		 * This logic is adapted from:
		 * https://stackoverflow.com/questions/43586360/how-to-reset-hibernate-sequence-generators
		 *
		 * If this code ever breaks, we've probably either upgraded Hibernate or changed the
		 * sequence generator for HFJ_RESOURCE. In either case, the fix is to figure out
		 * how to get access to the internal state of the optimizer and clear it.
		 */

		MappingMetamodelImpl metamodel = (MappingMetamodelImpl)entityManager.getMetamodel();

		EntityPersister persister = metamodel.entityPersister(ResourceTable.class);

		List<Component.ValueGenerationPlan> generationPlans = getFieldValue(persister.getGenerator(), "generationPlans");
		Component.ValueGenerationPlan plan = generationPlans.get(0);

		HapiSequenceStyleGenerator subGenerator = getFieldValue(plan, "subgenerator");

		PooledOptimizer optimizer = (PooledOptimizer) subGenerator.getOptimizer();
		Field noTenantStateField = findField(optimizer, "noTenantState");
		noTenantStateField.set(optimizer, null);

	}

	@SuppressWarnings("unchecked")
	private static <T> T getFieldValue(Object theObject, String theFieldName) throws IllegalAccessException {
		Field generationPlansField = findField(theObject, theFieldName);
		return (T) generationPlansField.get(theObject);
	}

	@Nonnull
	private static Field findField(Object theObject, String theFieldName) {
		Field generationPlansField = ReflectionUtils.findField(theObject.getClass(), theFieldName);
		assert generationPlansField != null;
		generationPlansField.setAccessible(true);
		return generationPlansField;
	}

}
