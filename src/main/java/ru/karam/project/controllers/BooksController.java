package ru.karam.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.karam.project.model.Book;
import ru.karam.project.model.Person;
import ru.karam.project.services.BooksService;
import ru.karam.project.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;


    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year",required = false) boolean sort,
                        Model model){
        if(page == null && booksPerPage == null)
            model.addAttribute("books",booksService.findAll(sort));
        else
            model.addAttribute("books",booksService.findALlDividedByPages(page,booksPerPage,sort));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model,
                       @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.findById(id));
        Optional<Person> bookOwner = booksService.getOwner(id);
        if(bookOwner.isPresent()){
            model.addAttribute("owner", bookOwner.get());
        }else {
            model.addAttribute("people_test", peopleService.findAll());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }
    @PostMapping()
    public String save(@ModelAttribute @Valid Book book,
            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Integer id){
        model.addAttribute("book",booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         @PathVariable("id") int id,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "books/edit";
        booksService.update(id,book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/set_owner")
    public String setUser(@PathVariable("id") int id,
                          @ModelAttribute("person") Person person){
        booksService.setOwner(id, person.getId());
        System.out.println("brooooooo im in setUser:::::::::"+person.getName());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id){
        booksService.releaseBook(id);
        return "redirect:/books/" + id;
    }
    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }
    @PostMapping("/search")
    public String search(Model model,@RequestParam("starting") String start){
        model.addAttribute("books",booksService.findByName(start));
        return "books/search";
    }
}
