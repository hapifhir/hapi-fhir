package ca.uhn.hapi.fhir.sql.hibernatesvc;

import ca.uhn.fhir.context.ConfigurationException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import org.apache.commons.lang3.Validate;
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
import org.hibernate.type.ComponentType;
import org.hibernate.type.CompositeType;
import org.hibernate.type.EmbeddedComponentType;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ConditionalIdMappingContributor implements org.hibernate.boot.spi.AdditionalMappingContributor {

	@Override
	public String getContributorName() {
		return "PkCleaningMappingContributor";
	}

	private final Set<String> myQualifiedIdRemovedColumnNames = new HashSet<>();

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
			Set<String> idRemovedColumnNames = new HashSet<>();
			Set<String> idRemovedProperties = new HashSet<>();

			String entityTypeName = nextEntry.getKey();
			Class<?> entityType = getType(entityTypeName);

			PersistentClass entityPersistentClass = nextEntry.getValue();
			Table table = entityPersistentClass.getTable();
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
					idRemovedColumnNames.addAll(removedProperty.getColumns().stream().map(Column::getName).collect(Collectors.toSet()));
					removedProperty.getColumns().stream().map(theColumn -> table.getName() + "#" + theColumn.getName()).forEach(myQualifiedIdRemovedColumnNames::add);
					idRemovedProperties.add(removedProperty.getName());
					i--;

					for (Column next : entityPersistentClass.getTable().getColumns()) {
						if (idRemovedColumnNames.contains(next.getName())) {
							next.setNullable(true);
						}
					}
				}
			}

			if (idRemovedColumns.isEmpty()) {
				continue;
			}

			identifier.getSelectables().removeIf(t -> idRemovedColumnNames.contains(t.getText()));

			Component identifierMapper = entityPersistentClass.getIdentifierMapper();
			if (identifierMapper != null) {
				identifierMapper.getProperties().removeIf(t -> idRemovedProperties.contains(t.getName()));
				identifierMapper.getSelectables().removeIf(t -> idRemovedColumnNames.contains(t.getText()));
				CompositeType type = identifierMapper.getType();
				if (type instanceof ComponentType) {
					ComponentType ect = (ComponentType) type;

					Component wrapped = new Component(identifierMapper.getBuildingContext(), identifierMapper);
					wrapped.setComponentClassName(identifierMapper.getComponentClassName());
					identifierMapper.getProperties().forEach(wrapped::addProperty);

					EmbeddedComponentType filtered = new EmbeddedComponentType(wrapped, ect.getOriginalPropertyOrder());
					filtered.injectMappingModelPart(ect.getMappingModelPart(), null);
					try {
						Class<? extends Component> identifierMapperClass = identifierMapper.getClass();
						Field field = identifierMapperClass.getDeclaredField("type");
						field.setAccessible(true);
						field.set(identifierMapper, filtered);
						field.set(wrapped, filtered);
					} catch (NoSuchFieldException | IllegalAccessException e) {
						throw new IllegalStateException(e);
					}

				}
			}

			PrimaryKey pk = table.getPrimaryKey();
			List<Column> pkColumns = pk.getColumns();
			pkColumns.removeIf(idRemovedColumns::contains);

			for (ForeignKey foreignKey : table.getForeignKeys().values()) {
				Value value = foreignKey.getColumn(0).getValue();
				if (value instanceof ToOne) {
					ToOne manyToOne = (ToOne) value;
//					String propertyName = manyToOne.getPropertyName();
//					Field field = getField(entityType, propertyName);
//					ConditionalIdRelation conditionalIdRelation = field.getAnnotation(ConditionalIdRelation.class);
//					if (conditionalIdRelation != null) {
//						foreignKey.getColumns().removeIf(t->t.getName().equals(conditionalIdRelation.column()));
//						manyToOne.getColumns().removeIf(t->t.getName().equals(conditionalIdRelation.column()));
//					}

//					manyToOne.getColumns().removeIf(t->idRemovedColumnNames.contains(t.getName()));
//					foreignKey.getColumns().removeIf(t->idRemovedColumnNames.contains(t.getName()));

				}
			}

		}

		// Adjust foreign keys
		for (var nextEntry : theMetadata.getEntityBindingMap().entrySet()) {
			PersistentClass entityPersistentClass = nextEntry.getValue();
			Table table = entityPersistentClass.getTable();
			for (ForeignKey foreignKey : table.getForeignKeys().values()) {
				Value value = foreignKey.getColumn(0).getValue();
				if (value instanceof ToOne) {
					ToOne manyToOne = (ToOne) value;

					String targetTableName = theMetadata.getEntityBindingMap().get(manyToOne.getReferencedEntityName()).getTable().getName();

					Class<?> entityType = getType(nextEntry.getKey());
					Field field = getField(entityType, manyToOne.getPropertyName());
					Validate.notNull(field, "Unable to find field %s on entity %s", manyToOne.getPropertyName(), entityType.getName());
					JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
					Set<String> columnNamesToRemoveFromFks = new HashSet<>();
					if (joinColumns != null) {

						for (JoinColumn joinColumn : joinColumns.value()) {
							String targetColumnName = joinColumn.referencedColumnName();
							String sourceColumnName = joinColumn.name();
							if (isBlank(targetColumnName)) {
								targetColumnName = sourceColumnName;
							}
							if (myQualifiedIdRemovedColumnNames.contains(targetTableName + "#" + targetColumnName)) {
								columnNamesToRemoveFromFks.add(sourceColumnName);
							}
						}
					}

					manyToOne.getColumns().removeIf(t-> columnNamesToRemoveFromFks.contains(t.getName()));
					foreignKey.getColumns().removeIf(t-> columnNamesToRemoveFromFks.contains(t.getName()));
				} else {

				}
			}
		}
	}

	@Nonnull
	private static Class<?> getType(String entityTypeName) {
		Class<?> entityType;
		try {
			entityType = Class.forName(entityTypeName);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		return entityType;
	}

	@Nullable
	private static Field getField(Class<?> theType, String theFieldName) {
		Field field;
		try {
			field = theType.getDeclaredField(theFieldName);
		} catch (NoSuchFieldException e) {
			try {
				field = theType.getField(theFieldName);
			} catch (NoSuchFieldException theE) {
				field = null;
			}
		}

		if (field == null && theType.getSuperclass() != null) {
			field = getField(theType.getSuperclass(), theFieldName);
		}

		return field;
	}
}
