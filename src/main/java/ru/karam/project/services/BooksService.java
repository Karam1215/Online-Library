package ru.karam.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karam.project.model.Book;
import ru.karam.project.model.Person;
import ru.karam.project.repositories.BookRepository;
import ru.karam.project.repositories.PeopleRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BooksService {
    private final BookRepository bookRepository;
    private final PeopleRepository personRepository;

    @Autowired
    public BooksService(BookRepository bookRepository, PeopleRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(boolean sort) {
        if (sort)
            return bookRepository.findAll(Sort.by("year"));
        else
            return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findALlDividedByPages(Integer page, Integer books_per_page, boolean sort) {
        if(sort)
            return bookRepository.findAll(PageRequest.of(page,books_per_page,
                    Sort.by("year"))).getContent();
        else
            return bookRepository.findAll(PageRequest.of(page,books_per_page)).getContent();
    }

    @Transactional(readOnly = true)
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }
    public void update(int id,Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }

    public Optional<Person> getOwner(int id){
        return bookRepository.findById(id).map(Book::getOwner);
    }

    public void setOwner(int book_id,int person_id){
        bookRepository.findById(book_id).ifPresent(book -> {
            Person owner = personRepository.findById(person_id).orElse(null);
            book.setOwner(owner);
            System.out.println("brooooooo im in setOwner:::::::::"+owner);
            book.setOwnedAt(Timestamp.from(Instant.now()));
            book.setExpired(false);
        });
    }
    public void releaseBook(int id){
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setExpired(false);
            book.setOwnedAt(null);
        });

    }
    public List<Book> findByName(String name){
        return bookRepository.findByTitleIgnoreCaseStartingWith(name);
    }
}
