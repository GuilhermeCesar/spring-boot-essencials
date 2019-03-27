package br.com.devdojo.model;

import java.util.ArrayList;
import static java.util.Arrays.*;
import java.util.List;

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

    public Student(String nome, int id) {
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
                asList(new Student("Deku"),new Student("Todoroki"))
        );
    }

}
