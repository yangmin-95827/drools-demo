package org.example.module;

public class School {

    private int count;

    private String name;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School(int count, String name) {
        this.count = count;
        this.name = name;
    }

    @Override
    public String toString() {
        return "School{" +
                "count=" + count +
                ", name='" + name + '\'' +
                '}';
    }
}
