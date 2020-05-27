package com.example.personscrud.controller;

import com.example.personscrud.domain.Person;
import com.example.personscrud.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.Optional;

@Controller
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Person> personList = personService.listAll();
        model.addAttribute("listPersons", personList);
        return "index";
    }

    @GetMapping("/new")
    public String showNewPersonPage(Model model) {
        Person person = new Person();
        model.addAttribute("thePerson", person);
        return "new_person";
    }

    @PostMapping("/save")
    public String savePerson(Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "error";
        personService.save(person);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable int id, Model model) {
        Optional<Person> person = personService.findPerson(id);
        if (person.isPresent()) {
            model.addAttribute("person_to_edit", person.get());
            return "edit_person";
        } else
            return "error";
    }

    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable long id) {
        personService.delete(id);
        return "redirect:/";
    }
}