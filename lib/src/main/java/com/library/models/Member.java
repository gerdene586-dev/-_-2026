package com.library.models;

public class Member {

    private int id;

    private String surname;

    private String name;

    public Member(int id, String surname, String name) {

        this.id = id;

        this.surname = surname;

        this.name = name;

    }

    public int getId() {

        return id;

    }

    public String getFullName() {

        return surname + " " + name;

    }

    @Override

    public String toString() {

        return getFullName();

    }

}