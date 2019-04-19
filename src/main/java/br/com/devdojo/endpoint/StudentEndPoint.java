package br.com.devdojo.endpoint;

import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("students")
public class StudentEndPoint {

    private final StudentRepository studentDao;

    @Autowired
    public StudentEndPoint(StudentRepository studentDao) {
        this.studentDao = studentDao;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        return new ResponseEntity(this.studentDao.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        this.verifyStudentExists(id);
        Student student = this.studentDao.findOne(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudensByName(@PathVariable String name) {
        return new ResponseEntity<>(this.studentDao.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Student student) {
        this.studentDao.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.verifyStudentExists(id);
        this.studentDao.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Student student) {
        this.verifyStudentExists(student.getId());
        this.studentDao.save(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void verifyStudentExists(Long id){
        if (this.studentDao.findOne(id) == null) {
            throw new ResourceNotFoundException("Student not found for ID: " + id);
        }
    }
}
