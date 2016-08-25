/**
 * 
 */
package com.strateratech.dhs.peerrate.entity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.hibernate.annotations.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strateratech.dhs.peerrate.testingsupport.ClassFinder;

/**
 * 
 * @author 2020
 * @date Jan 11, 2016 1:37:15 PM
 * @version 
 */
public class EntityModelStandardsTest {
	private static final Logger log = LoggerFactory.getLogger(EntityModelStandardsTest.class);
	private static final List<String> VALID_VALUES = Arrays.asList("varchar2", "char(1)");
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		
		List<Class<?>> classes = ClassFinder.find("com.strateratech.dhs.peerrate.entity");
		Assert.assertTrue("There must be at least 1 entity class",classes.size()>0);
		for (Class<?> clazz:classes) {
			if (!clazz.getCanonicalName().endsWith("Test")) {
				validateEntityClass(clazz);
			}
		}
		
	}

	/**
	 * @param clazz
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IOException 
	 */
	private void validateEntityClass(Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		List<Field> idFields = new ArrayList<Field>();
		log.debug("Analyzing {}",clazz.getCanonicalName());
		Annotation[] classAnnotations = clazz.getAnnotations();
		List<String> classAnnotationNames = convertAnnotationsToStringList(classAnnotations);
		validateClassRequirements(clazz, classAnnotationNames);
		Object o = clazz.newInstance();
		Assert.assertNotNull("this class must have a public noarg constructor", o);
		Field[] flds = clazz.getDeclaredFields();
		Assert.assertTrue("There must be at least 1 field on a table entity "+clazz.getCanonicalName(), flds.length > 0);
		for (Field fld: flds) {
			log.debug("found field {} {} {}", fld.getModifiers(), fld.getType().getSimpleName(), fld.getName());
			if (!Modifier.isStatic(fld.getModifiers())) {
				Assert.assertTrue("All fields should be private "+getFieldAsString(fld),
						Modifier.isPrivate(fld.getModifiers()));
				Annotation[] annotations = fld.getAnnotations();
				List<String> fieldAnnotationNames = convertAnnotationsToStringList(annotations);
				log.debug("found annotations {}", fieldAnnotationNames);
				if (Collections.frequency(fieldAnnotationNames, "Transient") != 1 && !Modifier.isTransient(fld.getModifiers())) {
					if (Collections.frequency(fieldAnnotationNames, "Id") == 1) {
						idFields.add(fld);
					}
						
					validateCommonFieldRequirements(clazz, o, fld, fieldAnnotationNames);
					if (!Collection.class.isAssignableFrom(fld.getType())) {
						validateNonCollectionField(clazz, o, fld, fieldAnnotationNames);
					}
					else {						
						validateCollectionField(clazz, o, fld, fieldAnnotationNames);
						
					}
				}
			}
			
			
		}
		Assert.assertTrue("Must be at least 1 field annotated as @Id "+clazz.getCanonicalName(),
				idFields.size() > 0);
	}

	/**
	 * @param clazz
	 * @param classAnnotationNames
	 * @throws IOException 
	 */
	private void validateClassRequirements(Class<?> clazz, List<String> classAnnotationNames) throws IOException {
		Assert.assertEquals("There should be 1 (and only one) @Entity annotation for "+clazz.getCanonicalName(),
				1, Collections.frequency(classAnnotationNames,"Entity"));
		Assert.assertEquals("There should be 1 (and only one) @Table annotation for "+clazz.getCanonicalName(),
				1,Collections.frequency(classAnnotationNames,"Table"));
		
		
		Table tableAnnotation = clazz.getAnnotation(Table.class);
	
		log.debug("Entity named {} maps to table {}", clazz.getCanonicalName(), tableAnnotation.name());
		// make sure table name is lower case
		Assert.assertTrue("Always define table names as lowercase",tableAnnotation.name().toLowerCase().equals(tableAnnotation.name()));
		// Identify db_unit file associated with table
		String dbUnitFile="dbunit/"+tableAnnotation.name()+".xml";
		InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream(dbUnitFile);
		Assert.assertTrue("There should be a dbunit load file for each entity.  This one is missing: "+dbUnitFile, 
				is != null && is.available() > 0);
		if (is != null) {
			IOUtils.closeQuietly(is);
		}

	}

	/**
	 * @param clazz
	 * @param o
	 * @param fld
	 * @param fieldAnnotationNames
	 * @param idFields
	 */
	private void validateCommonFieldRequirements(Class<?> clazz, Object o, Field fld, List<String> fieldAnnotationNames) {

		if (Collections.frequency(fieldAnnotationNames, "OneToMany") ==  1) {
			OneToMany oneToManyAnno = fld.getAnnotation(OneToMany.class);
			Assert.assertTrue("@OneToMany Annotation should always have a mappedBy value "+getFieldAsString(fld),
					!StringUtils.isBlank(oneToManyAnno.mappedBy()));
			Assert.assertTrue("@OneToMany Annotation should always have a targetEntity value "+getFieldAsString(fld),
					oneToManyAnno.targetEntity() != null);							
		}
		if (Collections.frequency(fieldAnnotationNames, "Enumerated") ==  1) {
			Enumerated enumAnno = fld.getAnnotation(Enumerated.class);
			Assert.assertEquals("@Enumerated Annotation should always be mapped by string not ordinal value "+getFieldAsString(fld),
					EnumType.STRING, enumAnno.value());
		}
		if (Enum.class.isAssignableFrom(fld.getType())) {
			Assert.assertTrue("Make sure all Enum return types have a proper @Enumerated annotation "+getFieldAsString(fld),
			Collections.frequency(fieldAnnotationNames, "Enumerated") ==  1);
		}

		
	}

	private void validateNonCollectionField(Class clazz, Object entityInstance, Field fld, List<String> fieldAnnotationNames) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object fldVal = PropertyUtils.getProperty(entityInstance, fld.getName());
		Assert.assertNull("Non-collection class is supposed to be null "+getFieldAsString(fld),fldVal);
		Object setVal = calculateMockValue(fld);
		if (setVal != null) {
			PropertyUtils.setProperty(entityInstance, fld.getName(), setVal);
			fldVal = PropertyUtils.getProperty(entityInstance, fld.getName());
			Assert.assertNotNull("Calling the setter above should have set this value"+getFieldAsString(fld),fldVal);
		}
		Assert.assertEquals("There should be one @Column or @JoinColumn annotation for each field "+getFieldAsString(fld),
				1, Collections.frequency(fieldAnnotationNames,"Column")
			+ Collections.frequency(fieldAnnotationNames,"JoinColumn"));
		if (Collections.frequency(fieldAnnotationNames, "Column") ==  1) {
			Assert.assertTrue("Every column must have a name! "+getFieldAsString(fld),
					!StringUtils.isBlank(fld.getAnnotation(Column.class).name()));
			if (fld.getAnnotation(Lob.class) != null) {
				Assert.assertTrue("All LOB columns need a column defintion "+getFieldAsString(fld),
						!StringUtils.isBlank(fld.getAnnotation(Column.class).columnDefinition()));								
				
			}
			if (Boolean.class.isAssignableFrom(fld.getType())) {			   
				Type t = fld.getAnnotation(Type.class);
				Assert.assertEquals("Booleans should always be defined as YES_NO fields "+getFieldAsString(fld), 
						"yes_no", t.type());
				//Assert.assertEquals("Boolean column definitions should be varchar2 "+getFieldAsString(fld),
						//"varchar2", fld.getAnnotation(Column.class).columnDefinition());
				Assert.assertTrue("Boolean column definitions should be varchar2 or char1 "+getFieldAsString(fld),
				        VALID_VALUES.contains(fld.getAnnotation(Column.class).columnDefinition()));
			}
			
			if (Date.class.isAssignableFrom(fld.getType())) {
				
				Assert.assertEquals("There should be one @Temporal annotation for each field "+getFieldAsString(fld),
						1, Collections.frequency(fieldAnnotationNames,"Temporal"));
				Assert.assertEquals("There should be one @DateTimeFormat annotation for each field "+getFieldAsString(fld),
						1, Collections.frequency(fieldAnnotationNames,"DateTimeFormat"));
			}
			
			
		}
		else {
			Assert.assertTrue("Every joincolumn must have a name! "+getFieldAsString(fld),
					!StringUtils.isBlank(fld.getAnnotation(JoinColumn.class).name()));
			Assert.assertTrue("Every joincolumn must have a reference column! "+getFieldAsString(fld),
					!StringUtils.isBlank(fld.getAnnotation(JoinColumn.class).referencedColumnName()));
			
		}

	}
	public void validateCollectionField(Class clazz, Object entityInstance,  Field fld, List<String> fieldAnnotationNames) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Assert.assertEquals("There should be one All Collections should be mapped as either @OneToMany or @ManyToMany "+getFieldAsString(fld),
				1, Collections.frequency(fieldAnnotationNames,"OneToMany")
				+ Collections.frequency(fieldAnnotationNames,"ManyToMany"));
		// Also make sure getter returns empty collection not null
		Collection col = (Collection)PropertyUtils.getProperty(entityInstance, fld.getName());
		Assert.assertNotNull("Collection getters must return non-null empty collections"+getFieldAsString(fld),
				col);
		Assert.assertEquals("Collections should return empty collections "+getFieldAsString(fld),
				0, col.size());

	}
	/**
	 * @param fld
	 * @return
	 */
	private Object calculateMockValue(Field fld) {
		Object val=null;
		if (Integer.class.isAssignableFrom(fld.getType())) {
			val = 0;
		}
		else if (Long.class.isAssignableFrom(fld.getType())) {
			val=0L;
		}
		else if (String.class.isAssignableFrom(fld.getType())) {
			val = "Setter Test";
		}
		else if (Date.class.isAssignableFrom(fld.getType())) {
			val = new Date();
		}
		else if (Boolean.class.isAssignableFrom(fld.getType())) {
			val = Boolean.FALSE;
		}
		return val;
	}

	/**
	 * Convert field to bean utils path expression
	 * @param fld
	 * @return
	 */
	private String getFieldAsString(Field fld) {
		return fld.getDeclaringClass().getSimpleName()+"."+fld.getName();
	}

	/**
	 * @param classAnnotations
	 * @return
	 */
	private List<String> convertAnnotationsToStringList(Annotation[] annotations) {
		List<String> names = new ArrayList<String>();
		for (Annotation a: annotations) {
			names.add(a.annotationType().getSimpleName());
		}
		return names;
	}

}
