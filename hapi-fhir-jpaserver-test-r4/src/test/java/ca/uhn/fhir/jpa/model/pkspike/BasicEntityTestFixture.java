package ca.uhn.fhir.jpa.model.pkspike;

import jakarta.annotation.Nonnull;

public class BasicEntityTestFixture<R extends IRootEntity<J>,J extends IJoinEntity<R>> {

	/** does this scenario support null partition_id? */
	boolean myNullPartitionSupportFlag = true;

	BasicEntityTestFixture(Class<R> theRootType, Class<J> theJoinType) {
		myRootType = theRootType;
		myJoinType = theJoinType;
	}

	public final Class<R> myRootType;
	public final Class<J> myJoinType;

	public R buildRootEntity() {
		return buildInstance(myRootType);
	}

	public J buildJoinEntity() {
		return buildInstance(myJoinType);
	}

	public boolean isSupportNullPartitionId() {
		return myNullPartitionSupportFlag;
	}

	public static <R extends IRootEntity<J>,J extends IJoinEntity<R>> BasicEntityTestFixture<R,J> build(Class<R> theRootType, Class<J> theJoinType) {
		return new BasicEntityTestFixture<>(theRootType, theJoinType);
	}

	public static <R extends IRootEntity<J>,J extends IJoinEntity<R>> BasicEntityTestFixture<R,J> buildNoNullPartition(Class<R> theRootType, Class<J> theJoinType) {
		BasicEntityTestFixture<R, J> entityFixture = new BasicEntityTestFixture<>(theRootType, theJoinType);
		entityFixture.myNullPartitionSupportFlag = false;
		return entityFixture;
	}


	static <T> @Nonnull T buildInstance(Class<T> theClass)  {
		try {
			return theClass.getDeclaredConstructor().newInstance();
		} catch (Exception theE) {
			throw new RuntimeException(theE);
		}
	}
}
