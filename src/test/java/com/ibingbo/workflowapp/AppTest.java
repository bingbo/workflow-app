package com.ibingbo.workflowapp;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Unit test for simple App.
 */

public class AppTest {

    private Map<Class<?>, Method> addressGetMethodMap = new ConcurrentHashMap<>();
    private Map<Class<?>, Method> addressSetMethodMap = new ConcurrentHashMap<>();
    private Map<Class<?>, Method> courseGetMethodMap = new ConcurrentHashMap<>();
    private Map<Class<?>, Method> courseSetMethodMap = new ConcurrentHashMap<>();
    private Map<Long, Course> courseMap = new HashMap<>();
    private Map<Long, Address> addressMap = new HashMap<>();

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception {

        Course course = new Course();
        course.setId(1L);
        course.setName("english");
        course.setScore(50);
        Course course1 = new Course();
        course1.setId(2L);
        course1.setName("chinese");
        course1.setScore(50);
        courseMap.put(course.getId(), course);
        courseMap.put(course1.getId(), course1);

        Address address = new Address();
        address.setId(1L);
        address.setCountry("china");
        address.setProvince("bj");
        Address address1 = new Address();
        address1.setId(2L);
        address1.setCountry("china");
        address1.setProvince("hn");
        addressMap.put(address.getId(), address);
        addressMap.put(address1.getId(), address1);
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setId(1L);
        student.setName("bill");
        student.setAddressId(1L);
        student.setCourseId(1L);
        students.add(student);
        Student student1 = new Student();
        student1.setId(2L);
        student1.setName("bing");
        student1.setCourseId(2L);
        student1.setAddressId(2L);
        students.add(student1);

        Field[] fields = Student.class.getDeclaredFields();
        for (Field field : fields) {
            Reference reference = field.getAnnotation(Reference.class);
            if (reference != null) {
                String idFieldName = reference.id();
                Class referenceClass = reference.type();
                if (field.getType().isAssignableFrom(Address.class)) {
                    String idGetMethodName = "get" + StringUtils.capitalize(idFieldName);
                    Method idMethod = field.getDeclaringClass().getDeclaredMethod(idGetMethodName);
                    addressGetMethodMap.put(Student.class, idMethod);

                    String setMethodName = "set" + StringUtils.capitalize(field.getName());
                    Method setMethod =
                            field.getDeclaringClass().getDeclaredMethod(setMethodName, new Class[] {field.getType()});
                    addressSetMethodMap.put(Student.class, setMethod);
                }
                if (field.getType().isAssignableFrom(Course.class)) {
                    String idGetMethodName = "get" + StringUtils.capitalize(idFieldName);
                    Method idMethod = field.getDeclaringClass().getDeclaredMethod(idGetMethodName);
                    courseGetMethodMap.put(Student.class, idMethod);

                    String setMethodName = "set" + StringUtils.capitalize(field.getName());
                    Method setMethod =
                            field.getDeclaringClass().getDeclaredMethod(setMethodName, new Class[] {field.getType()});
                    courseSetMethodMap.put(Student.class, setMethod);
                }

                //                System.out.println("********* getterName:" + idGetMethodName);
                //                Object idValue = idMethod.invoke(student);
                //                System.out.println("*************get field value:" + idValue);
                //                Course course = new Course();
                //                course.setId(1L);
                //                course.setName("english");
                //                course.setScore(50);
                //                course.setStudentId(studentId);
                //                String setterName = "set" + StringUtils.capitalize(field.getName());
                //                System.out.println("*******setterName:" + setterName);
                //                field.setAccessible(true);
                //                field.set(student, course);
                //                Method setMethod =
                //                        field.getDeclaringClass().getDeclaredMethod(setterName, new Class[] {field
                //                        .getType()});
                //                Object res = setMethod.invoke(student, course);
                //                System.out.println(student);
            }
        }
        this.handleMergeAddress(students);
        this.handleMergeCourse(students);
        assertTrue(true);
    }

    public void handleMergeCourse(List students) throws Exception {
        Class<?> mainClass = students.iterator().next().getClass();
        List<Long> addressIds = new ArrayList<>();
        Method getMethod = courseGetMethodMap.get(mainClass);
        for (Object student2 : students) {
            Long id = (Long) getMethod.invoke(student2);
            addressIds.add(id);
        }
        Method setMethod = courseSetMethodMap.get(mainClass);
        for (Object student2 : students) {
            Long id = (Long) getMethod.invoke(student2);
            setMethod.invoke(student2, courseMap.get(id));
        }
    }

    public void handleMergeAddress(List students) throws Exception {
        Class<?> mainClass = students.iterator().next().getClass();
        List<Long> addressIds = new ArrayList<>();
        Method getMethod = addressGetMethodMap.get(mainClass);
        for (Object student2 : students) {
            Long id = (Long) getMethod.invoke(student2);
            addressIds.add(id);
        }
        Method setMethod = addressSetMethodMap.get(mainClass);
        for (Object student2 : students) {
            Student stu = getOO(Student.class, student2);
            Long id = (Long) getMethod.invoke(student2);
            setMethod.invoke(student2, addressMap.get(id));
        }
    }

    public <T> T getOO(Class<T> clz, Object o) {
        if (clz.isInstance(o)) {
            return clz.cast(o);
        }
        return null;
    }

    class Address {

        private Long id;
        private String country;
        private String province;
        private String city;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }

    class Course {

        private Long id;
        private Integer score;
        private String name;
        private Long studentId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }

    }

    class Student {

        private Long id;
        private String name;
        private Long courseId;
        private Long addressId;

        @Reference(id = "courseId", type = Course.class)
        private Course courseInfo;
        @Reference(id = "addressId", type = Address.class)
        private Address addressInfo;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Course getCourseInfo() {
            return courseInfo;
        }

        public void setCourseInfo(Course courseInfo) {
            this.courseInfo = courseInfo;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }

        public Long getAddressId() {
            return addressId;
        }

        public void setAddressId(Long addressId) {
            this.addressId = addressId;
        }

        public Address getAddressInfo() {
            return addressInfo;
        }

        public void setAddressInfo(Address addressInfo) {
            this.addressInfo = addressInfo;
        }

    }

}
