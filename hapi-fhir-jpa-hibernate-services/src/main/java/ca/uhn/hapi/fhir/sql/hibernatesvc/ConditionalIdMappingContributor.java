package ca.uhn.hapi.fhir.sql.hibernatesvc;

import ca.uhn.fhir.context.ConfigurationException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import org.apache.commons.lang3.Validate;
import org.hibernate.boot.ResourceStreamLocator;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.AdditionalMappingContributions;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.internal.util.collections.IdentitySet;
import org.hibernate.mapping.BasicValue;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.DependantValue;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.ToOne;
import org.hibernate.mapping.UniqueKey;
import org.hibernate.mapping.Value;
import org.hibernate.service.UnknownServiceException;
import org.hibernate.type.ComponentType;
import org.hibernate.type.CompositeType;
import org.hibernate.type.EmbeddedComponentType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ConditionalIdMappingContributor implements org.hibernate.boot.spi.AdditionalMappingContributor {

	private static final Logger ourLog = LoggerFactory.getLogger(ConditionalIdMappingContributor.class);
	private final Set<String> myQualifiedIdRemovedColumnNames = new HashSet<>();

	/**
	 * Constructor
	 */
	public ConditionalIdMappingContributor() {
		super();
	}

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

		StandardServiceRegistry serviceRegistry =
				theMetadata.getBootstrapContext().getServiceRegistry();
		HapiHibernateDialectSettingsService hapiSettingsSvc;
		try {
			hapiSettingsSvc = serviceRegistry.getService(HapiHibernateDialectSettingsService.class);
		} catch (UnknownServiceException e) {
			return;
		}

		assert hapiSettingsSvc != null;
		if (!hapiSettingsSvc.isTrimConditionalIdsFromPrimaryKeys()) {
			return;
		}

		ClassLoaderService classLoaderService = serviceRegistry.getService(ClassLoaderService.class);

		removeConditionalIdProperties(classLoaderService, theMetadata);
	}

	@SuppressWarnings("unchecked")
	private void removeConditionalIdProperties(
			ClassLoaderService theClassLoaderService, InFlightMetadataCollector theMetadata) {

		// Adjust primary keys - this handles @IdClass PKs, which are the
		// ones we use in most places
		for (var nextEntry : theMetadata.getEntityBindingMap().entrySet()) {
			IdentitySet<Column> idRemovedColumns = new IdentitySet<>();
			Set<String> idRemovedColumnNames = new HashSet<>();
			Set<String> idRemovedProperties = new HashSet<>();

			String entityTypeName = nextEntry.getKey();
			Class<?> entityType = getType(theClassLoaderService, entityTypeName);

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
					throw new ConfigurationException(
							"Failed to find field " + fieldName + " on type: " + entityType.getName());
				}

				ConditionalIdProperty remove = field.getAnnotation(ConditionalIdProperty.class);
				if (remove != null) {
					Property removedProperty = properties.remove(i);
					idRemovedColumns.addAll(removedProperty.getColumns());
					idRemovedColumnNames.addAll(removedProperty.getColumns().stream()
							.map(Column::getName)
							.collect(Collectors.toSet()));
					removedProperty.getColumns().stream()
							.map(theColumn -> table.getName() + "#" + theColumn.getName())
							.forEach(myQualifiedIdRemovedColumnNames::add);
					idRemovedProperties.add(removedProperty.getName());
					i--;

					for (Column next : entityPersistentClass.getTable().getColumns()) {
						if (idRemovedColumnNames.contains(next.getName())) {
							next.setNullable(true);
						}
					}

					// We're removing it from the identifierMapper so we need to add it to the
					// entity class itself instead
					if (getField(entityType, removedProperty.getName()) != null) {
						entityPersistentClass.addProperty(removedProperty);
					}
				}
			}

			if (idRemovedColumns.isEmpty()) {
				continue;
			}

			identifier.getSelectables().removeIf(t -> idRemovedColumnNames.contains(t.getText()));
			identifier.getColumns().removeIf(t -> idRemovedColumnNames.contains(t.getName()));

			Component identifierMapper = entityPersistentClass.getIdentifierMapper();
			if (identifierMapper != null) {
				List<Property> finalPropertyList = identifierMapper.getProperties();
				finalPropertyList.removeIf(t -> idRemovedProperties.contains(t.getName()));
				identifierMapper.getSelectables().removeIf(t -> idRemovedColumnNames.contains(t.getText()));
				updateComponentWithNewPropertyList(identifierMapper, finalPropertyList);
			}

			PrimaryKey pk = table.getPrimaryKey();
			List<Column> pkColumns = pk.getColumns();
			removeColumns(pkColumns, idRemovedColumns::contains);
		}

		// Adjust composites - This handles @EmbeddedId PKs like JpaPid
		List<Component> registeredComponents = new ArrayList<>();
		theMetadata.visitRegisteredComponents(registeredComponents::add);

		for (Component c : registeredComponents) {
			Class<?> componentType = c.getComponentClass();
			String tableName = c.getTable().getName();

			Set<String> removedPropertyNames = new HashSet<>();
			for (Property property : new ArrayList<>(c.getProperties())) {
				Field field = getField(componentType, property.getName());
				assert field != null;
				ConditionalIdProperty annotation = field.getAnnotation(ConditionalIdProperty.class);
				if (annotation != null) {
					c.getProperties().remove(property);
					removedPropertyNames.add(property.getName());

					jakarta.persistence.Column column = field.getAnnotation(jakarta.persistence.Column.class);
					String columnName = column.name();
					myQualifiedIdRemovedColumnNames.add(tableName + "#" + columnName);

					PrimaryKey primaryKey = c.getTable().getPrimaryKey();
					primaryKey
							.getColumns()
							.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(tableName + "#" + t.getName()));

					for (Column nextColumn : c.getTable().getColumns()) {
						if (myQualifiedIdRemovedColumnNames.contains(tableName + "#" + nextColumn.getName())) {
							nextColumn.setNullable(true);
						}
					}

					List<Property> properties = c.getOwner().getProperties();
					for (Property nextProperty : properties) {
						if (nextProperty.getName().equals(property.getName())) {
							BasicValue value = (BasicValue) nextProperty.getValue();
							Field insertabilityField = getField(value.getClass(), "insertability");
							assert insertabilityField != null;
							insertabilityField.setAccessible(true);
							try {
								List<Boolean> insertability = (List<Boolean>) insertabilityField.get(value);
								insertability.set(0, Boolean.TRUE);
							} catch (IllegalAccessException e) {
								throw new IllegalStateException(e);
							}
						}
					}
				}
			}

			Type type = c.getType();
			if (type instanceof ComponentType) {

				String[] typePropertyNames = ((ComponentType) type).getPropertyNames();
				for (String propertyName : typePropertyNames) {
					if (removedPropertyNames.contains(propertyName)) {
						updateComponentWithNewPropertyList(c, c.getProperties());
						break;
					}
				}
			}
		}

		// Adjust relations with local filtered columns (e.g. ManyToOne)
		for (var nextEntry : theMetadata.getEntityBindingMap().entrySet()) {
			PersistentClass entityPersistentClass = nextEntry.getValue();
			Table table = entityPersistentClass.getTable();
			for (ForeignKey foreignKey : table.getForeignKeys().values()) {
				Value value = foreignKey.getColumn(0).getValue();
				if (value instanceof ToOne) {
					ToOne manyToOne = (ToOne) value;

					String targetTableName = theMetadata
							.getEntityBindingMap()
							.get(manyToOne.getReferencedEntityName())
							.getTable()
							.getName();
					Class<?> entityType = getType(theClassLoaderService, nextEntry.getKey());
					String propertyName = manyToOne.getPropertyName();
					Set<String> columnNamesToRemoveFromFks =
							determineFilteredColumnNamesInForeignKey(entityType, propertyName, targetTableName);

					removeColumns(manyToOne.getColumns(), t1 -> columnNamesToRemoveFromFks.contains(t1.getName()));
					removeColumns(foreignKey.getColumns(), t1 -> columnNamesToRemoveFromFks.contains(t1.getName()));

					columnNamesToRemoveFromFks.forEach(
							t -> myQualifiedIdRemovedColumnNames.add(table.getName() + "#" + t));

				} else {

					foreignKey
							.getColumns()
							.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(
									foreignKey.getReferencedTable().getName() + "#" + t.getName()));
				}
			}

			for (UniqueKey uniqueKey : table.getUniqueKeys().values()) {
				uniqueKey
						.getColumns()
						.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(table.getName() + "#" + t.getName()));
			}
		}

		for (Component c : registeredComponents) {
			String tableName = c.getTable().getName();

			c.getColumns().removeIf(t -> {
				String name = tableName + "#" + t.getName();
				return myQualifiedIdRemovedColumnNames.contains(name);
			});
			c.getSelectables().removeIf(t -> myQualifiedIdRemovedColumnNames.contains(tableName + "#" + t.getText()));
		}

		// Adjust relations with remote filtered columns (e.g. OneToMany)
		for (var nextEntry : theMetadata.getEntityBindingMap().entrySet()) {
			PersistentClass entityPersistentClass = nextEntry.getValue();

			for (Property property : entityPersistentClass.getProperties()) {
				Value propertyValue = property.getValue();
				if (propertyValue instanceof Collection) {
					Collection propertyValueBag = (Collection) propertyValue;
					KeyValue propertyKey = propertyValueBag.getKey();
					if (propertyKey instanceof DependantValue) {
						DependantValue dependantValue = (DependantValue) propertyKey;

						dependantValue
								.getColumns()
								.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(
										propertyValueBag.getCollectionTable().getName() + "#" + t.getName()));
					}
				} else if (propertyValue instanceof Component) {
					// Adjust properties, which accounts for things like @Nested properties with
					// filtered subproperties
					Component component = (Component) propertyValue;
					Set<String> columnNames = component.getColumns().stream()
							.map(t -> t.getName())
							.collect(Collectors.toSet());
					component
							.getSelectables()
							.removeIf(t -> (t instanceof Column) && !columnNames.contains(t.getText()));
				}
			}
		}
	}

	@Nonnull
	private Set<String> determineFilteredColumnNamesInForeignKey(
			Class<?> theEntityType, String thePropertyName, String theTargetTableName) {
		Field field = getField(theEntityType, thePropertyName);
		Validate.notNull(field, "Unable to find field %s on entity %s", thePropertyName, theEntityType.getName());
		JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
		Set<String> columnNamesToRemoveFromFks = new HashSet<>();
		if (joinColumns != null) {

			for (JoinColumn joinColumn : joinColumns.value()) {
				String targetColumnName = joinColumn.referencedColumnName();
				String sourceColumnName = joinColumn.name();
				if (isBlank(targetColumnName)) {
					targetColumnName = sourceColumnName;
				}
				if (myQualifiedIdRemovedColumnNames.contains(theTargetTableName + "#" + targetColumnName)) {
					columnNamesToRemoveFromFks.add(sourceColumnName);
				}
			}
		}
		return columnNamesToRemoveFromFks;
	}

	private static void updateComponentWithNewPropertyList(
			Component identifierMapper, List<Property> finalPropertyList) {
		CompositeType type = identifierMapper.getType();
		if (type instanceof ComponentType) {
			ComponentType ect = (ComponentType) type;

			Component wrapped = new Component(identifierMapper.getBuildingContext(), identifierMapper);
			wrapped.setComponentClassName(identifierMapper.getComponentClassName());
			finalPropertyList.forEach(wrapped::addProperty);

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

	private static void removeColumns(List<Column> theColumnList, Predicate<Column> theRemoveIfPredicate) {
		for (int listIndex = 0; listIndex < theColumnList.size(); listIndex++) {
			Column column = theColumnList.get(listIndex);
			if (theRemoveIfPredicate.test(column)) {
				theColumnList.remove(listIndex);
				for (int remainingIndex = listIndex; remainingIndex < theColumnList.size(); remainingIndex++) {
					Column remainingColumn = theColumnList.get(remainingIndex);
					if (remainingColumn.getTypeIndex() > 0) {
						remainingColumn.setTypeIndex(remainingColumn.getTypeIndex() - 1);
					}
				}
				listIndex--;
			}
		}
	}

	@Nonnull
	private static Class<?> getType(ClassLoaderService theClassLoaderService, String entityTypeName) {
		Class<?> entityType;
		entityType = theClassLoaderService.classForTypeName(entityTypeName);
		Validate.notNull(entityType, "Could not load type: %s", entityTypeName);
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
