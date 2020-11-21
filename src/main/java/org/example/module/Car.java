package org.example.module;

public class Car {

    private int discount;

    private Person person;

    public Car(int discount, Person person) {
        this.discount = discount;
        this.person = person;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Car{" +
                "discount=" + discount +
                ", person=" + person +
                '}';
    }
}
