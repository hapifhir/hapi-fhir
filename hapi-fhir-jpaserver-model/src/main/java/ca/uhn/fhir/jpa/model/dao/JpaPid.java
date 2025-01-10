/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.dao;

import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.apache.commons.collections4.ComparatorUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * JPA implementation of IResourcePersistentId.  JPA uses a Long as the primary key.  This class should be used in any
 * context where the pid is known to be a Long.
 */
@Embeddable
public class JpaPid extends BaseResourcePersistentId<Long> implements Comparable<JpaPid> {

	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_ID")
	@Column(name = "RES_ID", nullable = false)
	@GenericField(projectable = Projectable.YES)
	private Long myId;

	@PartitionedIdProperty
	@Column(name = PartitionablePartitionId.PARTITION_ID, nullable = false)
	private Integer myPartitionIdValue;

	private static final Comparator<JpaPid> COMPARATOR;

	static {
		Comparator<JpaPid> partitionComparator =
				Comparator.comparing(t -> defaultIfNull(t.myPartitionIdValue, Integer.MIN_VALUE));
		Comparator<JpaPid> idComparator = Comparator.comparing(t -> t.myId);
		COMPARATOR = ComparatorUtils.chainedComparator(List.of(partitionComparator, idComparator));
	}

	/**
	 * Constructor - Do not call this directly, only used for
	 * JPA instantiation
	 */
	public JpaPid() {
		super(null);
	}

	private JpaPid(Long theId) {
		super(null);
		myId = theId;
	}

	public JpaPid(Integer thePartitionIdValue, Long theId) {
		super(null);
		myId = theId;
		myPartitionIdValue = thePartitionIdValue;
	}

	private JpaPid(Long theId, Long theVersion) {
		super(theVersion, null);
		myId = theId;
	}

	private JpaPid(Long theId, String theResourceType) {
		super(theResourceType);
		myId = theId;
	}

	private JpaPid(Long theId, Long theVersion, String theResourceType) {
		super(theVersion, theResourceType);
		myId = theId;
	}

	public PartitionablePartitionId getPartitionablePartitionId() {
		return new PartitionablePartitionId(myPartitionIdValue, null);
	}

	public JpaPid setPartitionablePartitionId(PartitionablePartitionId thePartitionablePartitionId) {
		myPartitionIdValue = thePartitionablePartitionId != null ? thePartitionablePartitionId.getPartitionId() : null;
		return this;
	}

	public JpaPid setPartitionIdIfNotAlreadySet(Integer thePartitionId) {
		if (myPartitionIdValue == null && thePartitionId != null) {
			myPartitionIdValue = thePartitionId;
		}
		return this;
	}

	@Override
	public Integer getPartitionId() {
		return myPartitionIdValue;
	}

	public void setPartitionId(Integer thePartitionId) {
		myPartitionIdValue = thePartitionId;
	}

	/**
	 * Note that equals and hashCode for this object only consider the ID and Partition ID because
	 * this class gets used as cache keys
	 */
	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (!(theO instanceof JpaPid)) {
			return false;
		}
		JpaPid jpaPid = (JpaPid) theO;
		return Objects.equals(myId, jpaPid.myId) && Objects.equals(myPartitionIdValue, jpaPid.myPartitionIdValue);
	}

	/**
	 * Note that equals and hashCode for this object only consider the ID and Partition ID because
	 * this class gets used as cache keys
	 */
	@Override
	public int hashCode() {
		return Objects.hash(myId, myPartitionIdValue);
	}

	@Override
	public Long getId() {
		return myId;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	@Override
	public String toString() {
		String retVal = myPartitionIdValue != null ? myPartitionIdValue + "/" + myId.toString() : myId.toString();
		return retVal;
	}

	@Override
	public int compareTo(@Nonnull JpaPid theOther) {
		return COMPARATOR.compare(this, theOther);
	}

	public JpaPidFk toFk() {
		return JpaPidFk.fromPid(this);
	}

	public static List<Long> toLongList(JpaPid[] thePids) {
		return toLongList(Arrays.asList(thePids));
	}

	public static List<Long> toLongList(Collection<JpaPid> thePids) {
		List<Long> retVal = new ArrayList<>(thePids.size());
		for (JpaPid next : thePids) {
			retVal.add(next.getId());
		}
		return retVal;
	}

	public static Set<Long> toLongSet(Collection<JpaPid> thePids) {
		Set<Long> retVal = new HashSet<>(thePids.size());
		for (JpaPid next : thePids) {
			retVal.add(next.getId());
		}
		return retVal;
	}

	public static List<JpaPid> fromLongList(Collection<Long> theResultList) {
		List<JpaPid> retVal = new ArrayList<>(theResultList.size());
		for (Long next : theResultList) {
			retVal.add(fromId(next));
		}
		return retVal;
	}

	public static JpaPid fromId(Long theId) {
		return new JpaPid(theId);
	}

	public static JpaPid fromId(Long theResourceId, Integer thePartitionId) {
		return new JpaPid(thePartitionId, theResourceId);
	}

	public static JpaPid fromId(Long theResourceId, PartitionablePartitionId thePartitionId) {
		return new JpaPid(thePartitionId != null ? thePartitionId.getPartitionId() : null, theResourceId);
	}

	public static JpaPid fromIdAndVersion(Long theId, Long theVersion) {
		return new JpaPid(theId, theVersion);
	}

	public static JpaPid fromIdAndResourceType(Long theId, String theResourceType) {
		return new JpaPid(theId, theResourceType);
	}

	public static JpaPid fromIdAndVersionAndResourceType(Long theId, Long theVersion, String theResourceType) {
		return new JpaPid(theId, theVersion, theResourceType);
	}

	public static JpaPid fromId(IdAndPartitionId theId) {
		JpaPid retVal = new JpaPid(theId.getId());
		retVal.setPartitionIdIfNotAlreadySet(theId.getPartitionIdValue());
		return retVal;
	}
}
