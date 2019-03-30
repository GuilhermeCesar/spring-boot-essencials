package br.com.devdojo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class Student {

    private String nome;
    private int id;
    public static List<Student> studentList;

    static {
        studentRepository();
    }

    public Student(String nome) {
        this.nome = nome;
    }

    public Student() {
    }

    public Student(int id,String nome) {
        this(nome);
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private static void studentRepository(){
        studentList = new ArrayList<>(
                asList(new Student(1,"Deku"),new Student(2,"Todoroki"))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
