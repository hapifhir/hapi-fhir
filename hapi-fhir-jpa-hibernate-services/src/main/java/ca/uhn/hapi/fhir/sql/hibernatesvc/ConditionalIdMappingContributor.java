package ca.uhn.hapi.fhir.sql.hibernatesvc;

import ca.uhn.fhir.context.ConfigurationException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hibernate.boot.ResourceStreamLocator;
import org.hibernate.boot.spi.AdditionalMappingContributions;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.internal.util.collections.IdentitySet;
import org.hibernate.mapping.BasicValue;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.ToOne;
import org.hibernate.mapping.Value;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConditionalIdMappingContributor implements org.hibernate.boot.spi.AdditionalMappingContributor {

	@Override
	public String getContributorName() {
		return "PkCleaningMappingContributor";
	}

	@Override
	public void contribute(
		AdditionalMappingContributions theContributions,
		InFlightMetadataCollector theMetadata,
		ResourceStreamLocator theResourceStreamLocator,
		MetadataBuildingContext theBuildingContext) {

		HapiHibernateDialectSettingsService hapiSettingsSvc = theMetadata.getBootstrapContext().getServiceRegistry().getService(HapiHibernateDialectSettingsService.class);
		assert hapiSettingsSvc != null;
		if (!hapiSettingsSvc.isTrimConditionalIdsFromPrimaryKeys()) {
			return;
		}

		removeConditionalIdProperties(theMetadata);
	}

	private void removeConditionalIdProperties(InFlightMetadataCollector theMetadata) {
		for (var nextEntry : theMetadata.getEntityBindingMap().entrySet()) {
			IdentitySet<Column> idRemovedColumns = new IdentitySet<>();
			Set<String> idRemovedProperties = new HashSet<>();

			Class<?> entityType;
			try {
				entityType = Class.forName(nextEntry.getKey());
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}

			PersistentClass entityPersistentClass = nextEntry.getValue();
			if (entityPersistentClass.getIdentifier() instanceof BasicValue) {
				continue;
			}

			Component identifier = (Component) entityPersistentClass.getIdentifier();
			List<Property> properties = identifier.getProperties();
			for (int i = 0; i < properties.size(); i++) {
				Property property = properties.get(i);
				String fieldName = property.getName();
				Field field = getField(entityType, fieldName);
				if (field == null) {
					field = getField(identifier.getComponentClass(), fieldName);
				}
				if (field == null) {
					throw new ConfigurationException("Failed to find field " + fieldName + " on type: " + entityType.getName());
				}

				ConditionalIdProperty remove = field.getAnnotation(ConditionalIdProperty.class);
				if (remove != null) {
					Property removedProperty = properties.remove(i);
					idRemovedColumns.addAll(removedProperty.getColumns());
					idRemovedProperties.add(removedProperty.getName());
					i--;
				}
			}

			if (idRemovedColumns.isEmpty()) {
				return;
			}

			if (entityPersistentClass.getIdentifierMapper() != null) {
				entityPersistentClass.getIdentifierMapper().getProperties().removeIf(t -> idRemovedProperties.contains(t.getName()));
			}

			Table table = entityPersistentClass.getTable();
			PrimaryKey pk = table.getPrimaryKey();
			List<Column> pkColumns = pk.getColumns();
			pkColumns.removeIf(idRemovedColumns::contains);

			for (ForeignKey foreignKey : table.getForeignKeys().values()) {
				Value value = foreignKey.getColumn(0).getValue();
				if (value instanceof ToOne) {
					ToOne manyToOne = (ToOne) value;
					Field field = getField(entityType, manyToOne.getPropertyName());
					ConditionalIdRelation conditionalIdRelation = field.getAnnotation(ConditionalIdRelation.class);
					if (conditionalIdRelation != null) {
						foreignKey.getColumns().removeIf(t->t.getName().equals(conditionalIdRelation.column()));
						manyToOne.getColumns().removeIf(t->t.getName().equals(conditionalIdRelation.column()));
					}
				}
			}

		}
	}

	@Nullable
	private static Field getField(Class<?> theType, String theFieldName) {
		Field field;
		try {
			field = theType.getDeclaredField(theFieldName);
		} catch (NoSuchFieldException e) {
			field = null;
		}
		return field;
	}
}
