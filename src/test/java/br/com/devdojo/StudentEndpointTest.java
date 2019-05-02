package br.com.devdojo;

import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTest {

    @Autowired
    public TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class config{

        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder()
                    .basicAuthorization("toyo", "devdojo");
        }
    }

    @Test
    public void listStudentWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1","1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students", String.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    public void getStudentByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1","1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/1", String.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }
}
