package com.example.demo.lambda.stream.model;

public class People {

    private int jgid;
    private String name;
    private int age;

    public People(int jgid, String name, int age) {
        this.jgid = jgid;
        this.name = name;
        this.age = age;
    }

    public int getJgid() {
        return jgid;
    }

    public void setJgid(int jgid) {
        this.jgid = jgid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "People{" +
                "jgid=" + jgid +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
