package org.example.module;

public class Man extends Person {

    private String sex;

    public Man(Integer age) {
        super(age);
    }

    public Man(Integer age, String name) {
        super(age, name);
    }

    public Man(Integer age, String name,String sex) {
        super(age, name);
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
