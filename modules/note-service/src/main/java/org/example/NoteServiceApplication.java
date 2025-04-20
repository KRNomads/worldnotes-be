package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NoteServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceApplication.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(NoteServiceApplication.class, args);
        logger.info("------------------------ NoteServiceApplication Start ------------------------");
    }
}
