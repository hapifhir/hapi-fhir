/*-
 * #%L
 * HAPI FHIR JPA Hibernate Services
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.hapi.fhir.sql.hibernatesvc;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
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
import org.hibernate.mapping.Index;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Selectable;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
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
 * The TLDR here is that entities like <code>HFJ_RESOURCE</code> include a
 * compound PK class now that includes both the <code>RES_ID</code> and
 * the <code>PARTITION_ID</code> class in that PK, and the entities that
 * have foreign-key constraints to HFJ_RESOURCE have equivalent compound
 * FK relations. This class strips the <code>PARTITION_ID</code> column
 * out of these PK/FK entities when Database Partition Mode is not
 * enabled (basically preserving the existing behaviour from before
 * Database Partition Mode was introduced in HAPI FHIR 8.0.0).
 * </p>
 * <p>
 * Some notes on the logic here:
 * <ul>
 *    <li>
 *        If a field has {@link PartitionedIdProperty} on it, it is removed
 *        from the "identifier mapper", which is the place in the hibernate
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
 * entities. You can use this if you need to run a debugger on the whole
 * process against the actual JPA entities.
 * </p><p>
 * This class is automatically instantiated by Hibernate using the Java
 * Service Provider mechanism. See:
 * <code>META-INF/services/org.hibernate.boot.spi.AdditionalMappingContributor</code>
 * </p>
 *
 * @since 8.0.0
 */
public class DatabasePartitionModeIdFilteringMappingContributor
		implements org.hibernate.boot.spi.AdditionalMappingContributor {

	private final Set<TableAndColumnName> myQualifiedIdRemovedColumnNames = new HashSet<>();

	/**
	 * Constructor
	 */
	public DatabasePartitionModeIdFilteringMappingContributor() {
		super();
	}

	@Override
	public String getContributorName() {
		return getClass().getSimpleName();
	}

	/**
	 * This method is called automatically by Hibernate after it has finished scanning
	 * the annotations on the various entities.
	 *
	 * @param theContributions         Collector of the contributions.
	 * @param theMetadata              Current (live) metadata.  Can be used to access already known mappings.
	 * @param theResourceStreamLocator Delegate for locating XML resources via class-path lookup.
	 * @param theBuildingContext       Access to useful contextual references.
	 */
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

		if (hapiSettingsSvc == null) {
			throw new ConfigurationException(
					Msg.code(2601) + HapiHibernateDialectSettingsService.class.getSimpleName()
							+ " was not found in the service registry. The Hibernate EntityManager for HAPI FHIR must be constructed using HapiEntityManagerFactoryUtil.");
		}

		if (hapiSettingsSvc.isDatabasePartitionMode()) {
			return;
		}

		// Remove partition ID columns from entity tables
		ClassLoaderService classLoaderService = serviceRegistry.getService(ClassLoaderService.class);
		removePartitionedIdColumnsFromMetadata(classLoaderService, theMetadata);

		// Adjust indexes
		removeColumnsFromIndexes(theMetadata, classLoaderService);
	}

	private void removePartitionedIdColumnsFromMetadata(
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

		for (String nextEntityName :
				new TreeSet<>(theMetadata.getEntityBindingMap().keySet())) {

			PersistentClass entityPersistentClass =
					theMetadata.getEntityBindingMap().get(nextEntityName);
			Table table = entityPersistentClass.getTable();
			for (ForeignKey foreignKey : table.getForeignKeys().values()) {
				// Adjust relations with local filtered columns (e.g. ManyToOne)
				filterPartitionedIdsFromLocalFks(theClassLoaderService, theMetadata, foreignKey, table, nextEntityName);
			}

			for (Property property : entityPersistentClass.getProperties()) {
				Value value = property.getValue();
				if (value instanceof ToOne) {
					ToOne toOne = (ToOne) value;
					filterPropertiesFromToOneRelationship(
							theClassLoaderService, theMetadata, table, entityPersistentClass.getClassName(), toOne);
				}
			}

			for (UniqueKey uniqueKey : table.getUniqueKeys().values()) {
				// Adjust UniqueKey constraints, which are uniqueness
				// constraints automatically generated to support FKs on
				// @OneToOne mappings
				filterPartitionedIdsFromUniqueConstraints(uniqueKey, table);
			}
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
		for (Iterator<Property> iter = properties.iterator(); iter.hasNext(); ) {
			Property property = iter.next();
			String fieldName = property.getName();
			Field field = getField(entityType, fieldName);
			if (field == null) {
				field = getField(identifier.getComponentClass(), fieldName);
			}
			if (field == null) {
				throw new ConfigurationException(
						Msg.code(2598) + "Failed to find field " + fieldName + " on type: " + entityType.getName());
			}

			PartitionedIdProperty remove = field.getAnnotation(PartitionedIdProperty.class);
			if (remove != null) {
				iter.remove();
				idRemovedColumns.addAll(property.getColumns());
				idRemovedColumnNames.addAll(property.getColumns().stream()
						.map(c -> c.getName().toUpperCase(Locale.ROOT))
						.collect(Collectors.toSet()));
				property.getColumns().stream()
						.map(theColumn -> new TableAndColumnName(table.getName(), theColumn.getName()))
						.forEach(myQualifiedIdRemovedColumnNames::add);
				idRemovedProperties.add(property.getName());

				for (Column next : entityPersistentClass.getTable().getColumns()) {
					if (idRemovedColumnNames.contains(next.getName().toUpperCase(Locale.ROOT))) {
						next.setNullable(true);
					}
				}

				// We're removing it from the identifierMapper so we need to add it to the
				// entity class itself instead
				if (getField(entityType, property.getName()) != null) {
					entityPersistentClass.addProperty(property);
				}
			}
		}

		if (idRemovedColumns.isEmpty()) {
			return;
		}

		Component identifierMapper = entityPersistentClass.getIdentifierMapper();
		if (identifierMapper != null) {
			List<Property> finalPropertyList = identifierMapper.getProperties();
			finalPropertyList.removeIf(t -> idRemovedProperties.contains(t.getName()));
			updateComponentWithNewPropertyList(identifierMapper, finalPropertyList);
		}

		PrimaryKey pk = table.getPrimaryKey();
		List<Column> pkColumns = pk.getColumns();
		removeColumns(pkColumns, idRemovedColumns::contains);
	}

	@SuppressWarnings("unchecked")
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
				myQualifiedIdRemovedColumnNames.add(new TableAndColumnName(tableName, columnName));

				PrimaryKey primaryKey = c.getTable().getPrimaryKey();
				primaryKey
						.getColumns()
						.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(
								new TableAndColumnName(tableName, t.getName())));

				for (Column nextColumn : c.getTable().getColumns()) {
					if (myQualifiedIdRemovedColumnNames.contains(
							new TableAndColumnName(tableName, nextColumn.getName()))) {
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
							throw new IllegalStateException(Msg.code(2599) + e, e);
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
			ForeignKey theForeignKey,
			Table theTable,
			String theEntityTypeName) {
		Value value = theForeignKey.getColumn(0).getValue();
		if (value instanceof ToOne) {
			ToOne manyToOne = (ToOne) value;
			Set<String> columnNamesToRemoveFromFks = filterPropertiesFromToOneRelationship(
					theClassLoaderService, theMetadata, theTable, theEntityTypeName, manyToOne);
			removeColumns(
					theForeignKey.getColumns(),
					t1 -> columnNamesToRemoveFromFks.contains(t1.getName().toUpperCase(Locale.ROOT)));
		} else {

			theForeignKey
					.getColumns()
					.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(new TableAndColumnName(
							theForeignKey.getReferencedTable().getName(), t.getName())));
		}
	}

	@Nonnull
	private Set<String> filterPropertiesFromToOneRelationship(
			ClassLoaderService theClassLoaderService,
			InFlightMetadataCollector theMetadata,
			Table theTable,
			String theEntityTypeName,
			ToOne manyToOne) {
		String targetTableName = theMetadata
				.getEntityBindingMap()
				.get(manyToOne.getReferencedEntityName())
				.getTable()
				.getName();
		Class<?> entityType = getType(theClassLoaderService, theEntityTypeName);
		String propertyName = manyToOne.getPropertyName();
		Set<String> columnNamesToRemoveFromFks =
				determineFilteredColumnNamesInForeignKey(entityType, propertyName, targetTableName);

		removeColumns(
				manyToOne.getColumns(),
				t1 -> columnNamesToRemoveFromFks.contains(t1.getName().toUpperCase(Locale.ROOT)));

		columnNamesToRemoveFromFks.forEach(
				t -> myQualifiedIdRemovedColumnNames.add(new TableAndColumnName(theTable.getName(), t)));
		return columnNamesToRemoveFromFks;
	}

	private void filterPartitionedIdsFromUniqueConstraints(UniqueKey uniqueKey, Table table) {
		uniqueKey
				.getColumns()
				.removeIf(t ->
						myQualifiedIdRemovedColumnNames.contains(new TableAndColumnName(table.getName(), t.getName())));
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
							.removeIf(t -> myQualifiedIdRemovedColumnNames.contains(new TableAndColumnName(
									propertyValueBag.getCollectionTable().getName(), t.getName())));
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
				if (myQualifiedIdRemovedColumnNames.contains(
						new TableAndColumnName(theTargetTableName, targetColumnName))) {
					columnNamesToRemoveFromFks.add(sourceColumnName.toUpperCase(Locale.ROOT));
				}
			}
		}
		return columnNamesToRemoveFromFks;
	}

	private static void removeColumnsFromIndexes(
			InFlightMetadataCollector theMetadata, ClassLoaderService classLoaderService) {
		for (Map.Entry<String, PersistentClass> nextEntry :
				theMetadata.getEntityBindingMap().entrySet()) {

			Class<?> type = getType(classLoaderService, nextEntry.getKey());
			Table table = nextEntry.getValue().getTable();

			PartitionedIndexes partitionedIndexes = type.getAnnotation(PartitionedIndexes.class);
			if (partitionedIndexes != null) {
				for (PartitionedIndex partitionedIndex : partitionedIndexes.value()) {
					String indexName = partitionedIndex.name();
					Set<String> columnNames = Set.of(partitionedIndex.columns());
					assert columnNames.stream().allMatch(t -> t.equals(t.toUpperCase(Locale.ROOT)));

					Index index = table.getIndex(indexName);
					if (index != null) {

						List<Selectable> selectables = getFieldValue(index, "selectables");
						for (Iterator<Selectable> iter = selectables.iterator(); iter.hasNext(); ) {
							Column next = (Column) iter.next();
							if (!columnNames.contains(next.getName().toUpperCase(Locale.ROOT))) {
								iter.remove();
							}
						}

					} else {
						throw new ConfigurationException(
								Msg.code(2604) + "@PartitionedIndex refers to unknown index " + indexName);
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
			try {
				field = theType.getField(theFieldName);
			} catch (NoSuchFieldException theE) {
				field = null;
			}
		}

		if (field == null && theType.getSuperclass() != null) {
			field = getField(theType.getSuperclass(), theFieldName);
		}

		if (field != null) {
			field.setAccessible(true);
		}

		return field;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getFieldValue(Object theTarget, String theFieldName) {
		T selectables;
		try {
			selectables = (T) getField(theTarget.getClass(), theFieldName).get(theTarget);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(Msg.code(2603) + "Failed to access field " + theFieldName, e);
		}
		return selectables;
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
				throw new IllegalStateException(Msg.code(2600) + e, e);
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
	private static Class<?> getType(ClassLoaderService theClassLoaderService, String theEntityTypeName) {
		Class<?> entityType;
		entityType = theClassLoaderService.classForTypeName(theEntityTypeName);
		Validate.notNull(entityType, "Could not load type: %s", theEntityTypeName);
		return entityType;
	}

	private static class TableAndColumnName {
		private final String myTableName;
		private final String myColumnName;
		private final int myHashCode;

		private TableAndColumnName(String theTableName, String theColumnName) {
			myTableName = theTableName.toUpperCase(Locale.ROOT);
			myColumnName = theColumnName.toUpperCase(Locale.ROOT);
			myHashCode = Objects.hash(myTableName, myColumnName);
		}

		@Override
		public boolean equals(Object theO) {
			if (!(theO instanceof TableAndColumnName)) return false;
			TableAndColumnName that = (TableAndColumnName) theO;
			return Objects.equals(myTableName, that.myTableName) && Objects.equals(myColumnName, that.myColumnName);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		@Override
		public String toString() {
			return "[" + myTableName + "#" + myColumnName + "]";
		}
	}
}
