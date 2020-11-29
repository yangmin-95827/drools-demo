package org.example.module;

import java.util.*;

public class Person {

    private Integer age;

    private String name;

    private String time;

    private Date birthday;

    private String className;

    private List<Person> childList;
    private Map<String,Object> credentialMap;

    public Person(){
        this.childList = new ArrayList<>();
        this.credentialMap = new HashMap<>();
    }

    public Person(Integer age) {
        this.age = age;
    }

    public Person(Integer age,String name) {
        this();
        this.age = age;
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Person> getChildList() {
        return childList;
    }

    public void setChildList(List<Person> childList) {
        this.childList = childList;
    }

    public Map<String, Object> getCredentialMap() {
        return credentialMap;
    }

    public void setCredentialMap(Map<String, Object> credentialMap) {
        this.credentialMap = credentialMap;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", birthday=" + birthday +
                ", className='" + className + '\'' +
                ", childList=" + childList +
                ", credentialMap=" + credentialMap +
                '}';
    }
}
