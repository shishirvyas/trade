package com.srd.demo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@SpringBootTest
class DemoApplicationTest{

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // Ensure the application context loads without errors
        assertNotNull(applicationContext, "The application context should have loaded.");
    }
}
