package br.com.devdojo.endpoint;

import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/")
public class StudentEndPoint {

    private StudentRepository studentDao;

    @Autowired
    public StudentEndPoint(StudentRepository studentDao) {
        this.studentDao = studentDao;
    }

    @GetMapping("protected/students")
    public ResponseEntity<?> listAll(Pageable pageable) {
        System.out.println(studentDao.findAll());
        return new ResponseEntity(studentDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("protected/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        this.verifyStudentExists(id);
        Student student = this.studentDao.findOne(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "protected/students/findByName/{name}")
    public ResponseEntity<?> findStudensByName(@PathVariable String name) {
        return new ResponseEntity<>(this.studentDao.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping(path = "admin/students")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        this.studentDao.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("admin/students")
    public ResponseEntity<?> update(@RequestBody Student student) {
        this.verifyStudentExists(student.getId());
        this.studentDao.save(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "admin/students/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.verifyStudentExists(id);
        this.studentDao.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void verifyStudentExists(Long id) {
        if (this.studentDao.findOne(id) == null) {
            throw new ResourceNotFoundException("Student not found for ID: " + id);
        }
    }
}
