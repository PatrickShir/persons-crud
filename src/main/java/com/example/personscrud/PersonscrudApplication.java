package com.example.personscrud;

import com.example.personscrud.domain.Person;
import com.example.personscrud.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PersonscrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonscrudApplication.class, args);
    }

    @Bean
    public CommandLineRunner setUp(PersonRepository personRepository) {
        if (personRepository.findAll().size() == 0)
            return (args -> {
                personRepository.save(new Person(1l, "Pedram", "Shirforoushan", "Street 1","Student",90));
                personRepository.save(new Person(2l, "Allan", "Jamil", "Street 2","Student",85));
                personRepository.save(new Person(3l, "Hugo", "Lindmark", "Street 3","Student",80));
            });
        return null;
    }
}

