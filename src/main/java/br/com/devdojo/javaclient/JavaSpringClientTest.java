package br.com.devdojo.javaclient;

import br.com.devdojo.model.Student;

import java.util.List;

public class JavaSpringClientTest {

    public static void main(String args[]) {
        Student studentPost = new Student();
        studentPost.setName("Johh Wich");
        studentPost.setEmail("john@pencil.com");

        JavaClientDao dao = new JavaClientDao();

//        System.out.println(dao.findById(2L));
        List<Student> studentList = dao.listAll();
        System.out.println(studentList);
//        System.out.println(dao.save(studentPost));


    }
}
