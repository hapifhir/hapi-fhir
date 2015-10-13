package ca.uhn.fhir.util;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import ca.uhn.fhir.util.reflection.IBeanUtils;
import ca.uhn.fhir.util.reflection.JavaBeansBeanUtil;
import ca.uhn.fhir.util.reflection.JavaReflectBeanUtil;

public class BeanUtilTest {

	@Test
	public void testFindAccessor() throws Exception {
		JavaBeansBeanUtil javaBeansBeanUtil = new JavaBeansBeanUtil();
		testBeanUtilsAccessor(javaBeansBeanUtil);
		JavaReflectBeanUtil javaReflectBeanUtil = new JavaReflectBeanUtil();
		testBeanUtilsAccessor(javaReflectBeanUtil);
		assertNotNull(BeanUtils.findAccessor(BeanUtilTestClass.class, String.class, "field"));
		Method jbMGet = javaBeansBeanUtil.findAccessor(BeanUtilTestClass.class, String.class, "field");
		Method jrMGet = javaReflectBeanUtil.findAccessor(BeanUtilTestClass.class, String.class, "field");
		assertNotNull(jbMGet);
		assertNotNull(jrMGet);
		assertEquals(jbMGet, jrMGet);
	}

	@Test
	public void testFindMutator() throws Exception {
		JavaBeansBeanUtil javaBeansBeanUtil = new JavaBeansBeanUtil();
		testBeanUtilsMutator(javaBeansBeanUtil);
		JavaReflectBeanUtil javaReflectBeanUtil = new JavaReflectBeanUtil();
		testBeanUtilsMutator(javaReflectBeanUtil);
		assertNotNull(BeanUtils.findMutator(BeanUtilTestClass.class, String.class, "field"));
		Method jbMSet = javaBeansBeanUtil.findMutator(BeanUtilTestClass.class, String.class, "field");
		Method jrMSet = javaReflectBeanUtil.findMutator(BeanUtilTestClass.class, String.class, "field");
		assertNotNull(jbMSet);
		assertNotNull(jrMSet);
		assertEquals(jbMSet, jrMSet);
	}
	
	private void testBeanUtilsAccessor(IBeanUtils util) throws Exception {
		assertNotNull(util.findAccessor(BeanUtilTestClass.class, String.class, "field"));
		try {
			assertNull(util.findAccessor(BeanUtilTestClass.class, String.class, "fieldX"));
			fail("Field is not in class");
		} catch (NoSuchFieldException e) { }
		try {
			assertNull(util.findAccessor(BeanUtilTestClass.class, Integer.class, "field"));
			fail("Field is in class, but we expect Integer as return type");
		} catch (NoSuchFieldException e) { }
	}
	
	private void testBeanUtilsMutator(IBeanUtils util) throws Exception {
		assertNotNull(util.findMutator(BeanUtilTestClass.class, String.class, "field"));
		try {
			assertNull(util.findMutator(BeanUtilTestClass.class, String.class, "fieldX"));
			fail("Field is not in class");
		} catch (NoSuchFieldException e) { }
		try {
			assertNull(util.findMutator(BeanUtilTestClass.class, Integer.class, "field"));
			fail("Field is in class, but we expect Integer as parameter type");
		} catch (NoSuchFieldException e) { }
	}
	
	public static class BeanUtilTestClass {
		private String myField;
		
		public String getField() {
			return myField;
		}
		
		public void setField(String value) {
			this.myField = value;
		}
	}
}
