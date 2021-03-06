package br.com.devdojo;


import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createSholudPersistData() {
        Student student = new Student("guilherme+cesar", "guilherme.cesar@guiherme.me");
        this.studentDao.save(student);
        Assertions.assertThat(student.getId()).isNotNull();
        Assertions.assertThat(student.getName()).isEqualTo("guilherme+cesar");
        Assertions.assertThat(student.getEmail()).isEqualTo("guilherme.cesar@guiherme.me");

    }

    @Test
    public void deleteShouldRemoveData() {
        Student student = new Student("guilherme+cesar", "guilherme.cesar@guiherme.me");
        studentDao.save(student);
        studentDao.delete(student);

        Assertions.assertThat(studentDao.findOne(student.getId())).isNull();
    }


    @Test
    public void updateShouldChangeAndPersistData() {
        Student student = new Student("guilherme+cesar", "guilherme.cesar@guiherme.me");
        this.studentDao.save(student);
        student.setName("guilherme cesar 2");
        student.setEmail("guilherme@guilherme.me");
        this.studentDao.save(student);
        student = this.studentDao.findOne(student.getId());
        Assertions.assertThat(student.getName()).isEqualTo("guilherme cesar 2");
        Assertions.assertThat(student.getEmail()).isEqualTo("guilherme@guilherme.me");
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
        Student student = new Student("GUILHERME1", "guilherme.cesar@guiherme.me");
        Student student2 = new Student("guilherme2", "guilherme@guiherme.me");
        this.studentDao.save(student);
        this.studentDao.save(student2);

        List<Student> students = this.studentDao.findByNameIgnoreCaseContaining("GUILHERME");

        Assertions.assertThat(students.size()).isEqualTo(2);
    }

    @Test
    public void createWhenNameIsNullShouldThrowConstraintViolationException(){
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("O campo nome do estudante é obrigatório");
        Student student = this.studentDao.save(new Student());
    }

    @Test
    public void createWhenEmailIsNullShouldThrowConstraintViolationException(){
        thrown.expect(ConstraintViolationException.class);
        Student student = new Student();
        student.setName("Guilherme césar");
        studentDao.save(student);
    }

    @Test
    public void createWhenEmailIsNotValidShouldThrowConstraintViolationException(){
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Digite um e-mail valido");
        Student student = new Student();
        student.setName("Guilherme");
        student.setEmail("guilherme.a");
        studentDao.save(student);
    }
}