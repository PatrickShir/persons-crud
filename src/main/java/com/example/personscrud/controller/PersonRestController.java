package com.example.personscrud.controller;

import com.example.personscrud.domain.Person;
import com.example.personscrud.domain.Response;
import com.example.personscrud.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/persons")
public class PersonRestController {
    PersonService personService;

    @Autowired
    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> allPersons() {
        return personService.listAll();
    }

    @GetMapping("/{id}")
    public Person findPersonByID(@PathVariable int id) {
        Optional<Person> person = personService.findPerson(id);
        Person newPerson = null;

        if (person.isPresent())
            newPerson = person.get();

        if (!newPerson.hasLink("find_person")) {
            Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonRestController.class).findPersonByID(id)).withSelfRel();
            newPerson.add(link);
        }
        if (!newPerson.hasLink("all_persons")) {
            Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonRestController.class).allPersons()).withRel("all_persons");
            newPerson.add(link);
        }
        if (!newPerson.hasLink("add_person")) {
            Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonRestController.class).addPerson(newPerson)).withRel("add_person");
            newPerson.add(link);
        }
        if (!newPerson.hasLink("delete_person")) {
            Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonRestController.class).deleteByID(id)).withRel("delete_person");
            newPerson.add(link);
        }

        return newPerson;
    }

    @PostMapping("/add")
    public Response addPerson(@RequestBody Person person) {
        Response response = new Response("Person added!", false);
        personService.save(person);
        response.setStatus(Boolean.TRUE);
        return response;
    }

    @DeleteMapping("delete/{id}")
    public Response deleteByID(@PathVariable int id) {
        Response response = new Response("Person deleted!", false);

        int indexToRemove = -1;
        for (int i = 0; i < personService.listAll().size(); i++) {
            if (personService.listAll().get(i).getId() == id)
                indexToRemove = i;
        }
        if (indexToRemove != -1) {
            personService.listAll().remove(indexToRemove);
            personService.delete(id);
            response.setStatus(true);
        } else
            response.setMessage("Not found");

        return response;
    }
}