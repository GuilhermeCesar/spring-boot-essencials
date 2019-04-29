package br.com.devdojo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    private final static  String SENHA = "123456";

    @Test
    public void contextLoads() {
        BCryptPasswordEncoder encoderPassword = new BCryptPasswordEncoder();
        System.out.println(encoderPassword.encode(SENHA));
    }

}
