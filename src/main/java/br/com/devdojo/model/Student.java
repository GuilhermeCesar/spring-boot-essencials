package br.com.devdojo.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;

@Entity
public class Student extends AbstractEntity {

    @NotEmpty(message = "O campo nome do estudante é obrigatório")
    private String name;

    @NotEmpty
    @Email(message = "Digite um e-mail valido")
    private String email;


    public Student() {
    }

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Student(Long id, String name, String email) {
        this(name, email);
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
