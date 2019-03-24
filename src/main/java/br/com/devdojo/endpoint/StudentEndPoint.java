package br.com.devdojo.endpoint;

import br.com.devdojo.model.Student;
import br.com.devdojo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("student")
public class StudentEndPoint {

    @Autowired
    private DateUtil dateUtil;

    @GetMapping("/list")
    public List<Student> listAll(){
//        System.out.println("============"+this.dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));

        return asList(new Student("Deku"),new Student("Todoroki"));
    }
}
