package ru.karam.project.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.karam.project.model.Person;
import ru.karam.project.services.BooksService;
import ru.karam.project.services.PeopleService;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model) {
        System.out.println("hereeeeeeeeeeeeeeeeee");
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("people", peopleService.findById(id));
        model.addAttribute("books", peopleService.getCurrentBooks(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping()
    public String save(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("person", peopleService.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         @PathVariable("id") int id,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.update(id,person);
        return "redirect:/people";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/people";
    }

}
