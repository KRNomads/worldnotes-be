package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoteApplication {

    private static final Logger logger = LoggerFactory.getLogger(NoteApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
        logger.info("------------------------ world note Server Start ------------------------");
    }
}
