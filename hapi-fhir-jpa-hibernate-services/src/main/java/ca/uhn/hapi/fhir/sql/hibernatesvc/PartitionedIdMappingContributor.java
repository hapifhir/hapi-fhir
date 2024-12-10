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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This is an {@link org.hibernate.boot.spi.AdditionalMappingContributor} which
 * examines the scanned metamodel that hibernate generates as it scans the
 * annotations on our entity classes, and when configured to do so,
 * filters out the fields/columns that are marked with {@link PartitionedIdProperty}
 * so that they no longer appear in PKs and FK.
 * <p>
 * Some notes on the logic here:
 * <ul>
 *    <li>
 *        If a field has {@link PartitionedIdProperty} on it, it is removed
 *        from the "identifier mapper", which is the place in hibernate's
 *        metamodel where the ID columns live.
 *    </li>
 *    <li>
 *        It's important to also have a second "normal" @Column in the same
 *        entity that has the same column name and datatype. This ensures
 *        that the column is included both in partitioned ID mode and in
 *        non-partitioned ID mode.
 *    </li>
 *    <li>
 *        If the field is stripped from the identifier mapper, any columns
 *        with the same column name on the same entity will be marked as
 *        nullable=true.
 *    </li>
 *    <li>
 *        Columns which are filtered from the identifier mapper are also
 *        filtered from foreign key definitions.
 *    </li>
 * </ul>
 * <p>
 * If you need to modify or troubleshoot this class, or want to run a
 * debugger through it, you have two options: There is a set of focused
 * entity classes testing parts of it in {@literal GenerateDdlMojoTest}
 * in {@literal hapi-tinder-plugin/src/test/java}. There is also
 * a unit test called {@literal JpaDdlTest} in {@literal hapi-tinder-test}
 * which runs this contributor and verifies the results against the real
 * entities.
 */
public class PartitionedIdMappingContributor implements org.hibernate.boot.spi.AdditionalMappingContributor {

	private final Set<String> myQualifiedIdRemovedColumnNames = new HashSet<>();

	/**
	 * Constructor
	 */
	public PartitionedIdMappingContributor() {
		super();
	}

	@Override
	public String getContributorName() {
		return "PartitionedIdMappingContributor";
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
			filterPartitionedIdsFromIdClassPks(theClassLoaderService, nextEntry);
		}

		// Adjust composites - This handles @EmbeddedId PKs like JpaPid
		List<Component> registeredComponents = new ArrayList<>();
		theMetadata.visitRegisteredComponents(registeredComponents::add);
		for (Component c : registeredComponents) {
			filterPartitionedIdsFromCompositeComponents(c);
		}

		for (var nextEntry : theMetadata.getEntityBindingMap().entrySet()) {
			PersistentClass entityPersistentClass = nextEntry.getValue();
			Table table = entityPersistentClass.getTable();
			for (ForeignKey foreignKey : table.getForeignKeys().values()) {
				// Adjust relations with local filtered columns (e.g. ManyToOne)
				filterPartitionedIdsFromLocalFks(theClassLoaderService, theMetadata, nextEntry, foreignKey, table);
			}

			for (UniqueKey uniqueKey : table.getUniqueKeys().values()) {
				// Adjust UniqueKey constraints, which are uniqueness
				// constraints automatically generated to support FKs on
				// @OneToOne mappings
				filterPartitionedIdsFromUniqueConstraints(uniqueKey, table);
			}
		}

		for (Component c : registeredComponents) {
			String tableName = c.getTable().getName();

			// FIXME: needed?
			//			c.getColumns().removeIf(t -> {
			//				String name = tableName + "#" + t.getName();
			//				return myQualifiedIdRemovedColumnNames.contains(name);
			//			});
			//			c.getSelectables().removeIf(t -> myQualifiedIdRemovedColumnNames.contains(tableName + "#" +
			// t.getText()));
		}

		// Adjust relations with remote filtered columns (e.g. OneToMany)
		for (var nextEntry : theMetadata.getEntityBindingMap().entrySet()) {
			PersistentClass entityPersistentClass = nextEntry.getValue();
			filterPartitionedIdsFromRemoteFks(entityPersistentClass);
		}
	}

	private void filterPartitionedIdsFromIdClassPks(
			ClassLoaderService theClassLoaderService, Map.Entry<String, PersistentClass> nextEntry) {
		IdentitySet<Column> idRemovedColumns = new IdentitySet<>();
		Set<String> idRemovedColumnNames = new HashSet<>();
		Set<String> idRemovedProperties = new HashSet<>();

		String entityTypeName = nextEntry.getKey();
		Class<?> entityType = getType(theClassLoaderService, entityTypeName);

		PersistentClass entityPersistentClass = nextEntry.getValue();
		Table table = entityPersistentClass.getTable();
		if (entityPersistentClass.getIdentifier() instanceof BasicValue) {
			return;
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

			PartitionedIdProperty remove = field.getAnnotation(PartitionedIdProperty.class);
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
			return;
		}

		// FIXME: needed?
		//		identifier.getSelectables().removeIf(t -> idRemovedColumnNames.contains(t.getText()));
		//		identifier.getProperties().removeIf(t -> idRemovedColumnNames.contains(t.getText()));
		//		identifier.getColumns().removeIf(t -> idRemovedColumnNames.contains(t.getName()));

		Component identifierMapper = entityPersistentClass.getIdentifierMapper();
		if (identifierMapper != null) {
			List<Property> finalPropertyList = identifierMapper.getProperties();
			finalPropertyList.removeIf(t -> idRemovedProperties.contains(t.getName()));
			// FIXME: needed?
			//			identifierMapper.getSelectables().removeIf(t -> idRemovedColumnNames.contains(t.getText()));
			updateComponentWithNewPropertyList(identifierMapper, finalPropertyList);
		}

		PrimaryKey pk = table.getPrimaryKey();
		List<Column> pkColumns = pk.getColumns();
		removeColumns(pkColumns, idRemovedColumns::contains);
	}

	private void filterPartitionedIdsFromCompositeComponents(Component c) {
		Class<?> componentType = c.getComponentClass();
		String tableName = c.getTable().getName();

		Set<String> removedPropertyNames = new HashSet<>();
		for (Property property : new ArrayList<>(c.getProperties())) {
			Field field = getField(componentType, property.getName());
			assert field != null;
			PartitionedIdProperty annotation = field.getAnnotation(PartitionedIdProperty.class);
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

	private void filterPartitionedIdsFromLocalFks(
			ClassLoaderService theClassLoaderService,
			InFlightMetadataCollector theMetadata,
			Map.Entry<String, PersistentClass> nextEntry,
			ForeignKey foreignKey,
			Table table) {
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

			columnNamesToRemoveFromFks.forEach(t -> myQualifiedIdRemovedColumnNames.add(table.getName() + "#" + t));

		} else {

			foreignKey
					.getColumns()
					.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(
							foreignKey.getReferencedTable().getName() + "#" + t.getName()));
		}
	}

	private void filterPartitionedIdsFromUniqueConstraints(UniqueKey uniqueKey, Table table) {
		uniqueKey
				.getColumns()
				.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(table.getName() + "#" + t.getName()));
	}

	private void filterPartitionedIdsFromRemoteFks(PersistentClass entityPersistentClass) {
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
				Set<String> columnNames =
						component.getColumns().stream().map(Column::getName).collect(Collectors.toSet());
				// FIXME: needed?
				//				component.getSelectables().removeIf(t -> (t instanceof Column) &&
				// !columnNames.contains(t.getText()));
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
