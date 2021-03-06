package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTokenTest {

    @Autowired
    public TestRestTemplate restTemplate;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @Before
    public void configProtectedHeaders() {
        String str = " {\"username\": \"Oda\",\"password\":\"devdojo\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.protectedHeader = new HttpEntity<>(headers);
    }

    @Before
    public void configAdminHeaders() {
        String str = " {\n" +
                "                \"username\": \"toyo\",\n" +
                "                \"password\":\"devdojo\"\n" +
                "    }";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }


    @Before
    public void configWrongHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "111111");
        this.wrongHeader = new HttpEntity<>(headers);
    }


    @Test
    public void listStudentWhenTokenisIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = this.restTemplate.exchange("/v1/protected/students", GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getStudentByIdWhenTokenAreIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/1", GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listStudentByIdWhenTokenIsCorrectShouldReturnStatusCode200() {
        List<Student> students = asList(
                new Student(1L, "Legolas", "legolas@lotr.com"),
                new Student(2L, "Aragorn", "aragorn@lotr.com")
        );

        BDDMockito.when(studentRepository.findAll())
                .thenReturn(students);
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/", GET, protectedHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }


    @Test
    public void getStudentByIdWhenTokenCorrectShouldReturnStatusCode200() {
        BDDMockito.when(studentRepository.findOne(2L))
                .thenReturn(new Student(2L, "Aragorn", "aragorn@lotr.com"));
        ResponseEntity<Student> response =
                restTemplate
                        .exchange("/v1/protected/students/2",
                                GET, protectedHeader, Student.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentByIdWhenTokenIsCorrectShouldReturnStatusCode404() {
        ResponseEntity<Student> response =
                restTemplate
                        .exchange("/v1/protected/students/{id}", GET, protectedHeader, Student.class, -1);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    //    TODO parei aqui
    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistShouldStatusCode204() {
        BDDMockito.when(studentRepository.findOne(2l)).thenReturn(new Student(2L, "Aragorn", "aragorn@lotr.com"));
        BDDMockito.doNothing().when(studentRepository).delete(2L);

        ResponseEntity<String> exchange = restTemplate
                .exchange("/v1/admin/students/2",
                        DELETE, adminHeader, String.class);

        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExitShouldStatusCode404() throws Exception {
        String token = adminHeader.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

        BDDMockito.doNothing().when(studentRepository).delete(1L);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/v1/admin/students/{id}", -1L)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteWhenUserDoesNotHaveHasRoleAdminShouldStatusCode403() throws Exception {
        String token = protectedHeader.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

        BDDMockito.doNothing().when(studentRepository).delete(1L);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/v1/admin/students/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() throws Exception {
        Student student = new Student(3L, null, "sam@lotr.com");

        BDDMockito.when(studentRepository.save(student))
                .thenReturn(student);

        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/v1/admin/students/",
                        POST,
                        new HttpEntity<>(student, adminHeader.getHeaders()),
                        String.class
                );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("O campo nome do estudante é obrigatório");
    }

    @Test
    public void createShouldPersistDataAndReturnStatusCode200() throws Exception {
        Student student = new Student(3L, "Sam", "sam@lotr.com");

        BDDMockito.when(studentRepository.save(student))
                .thenReturn(student);

        ResponseEntity<Student> response = restTemplate
                .exchange("/v1/admin/students/",
                        POST,
                        new HttpEntity<>(student, adminHeader.getHeaders()),
                        Student.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody()).isNotNull();
    }
}
