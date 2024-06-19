package ru.karam.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karam.project.model.Book;
import ru.karam.project.model.Person;
import ru.karam.project.repositories.PeopleRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {
    private final PeopleRepository personRepository;

    @Autowired
    public PeopleService(PeopleRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> findAll(){
        System.out.println("hereeeeeeeeeeeeeeeeee");
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Person findById(Integer id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public void update(int id,Person updatedPerson) {
            updatedPerson.setId(id);
            personRepository.save(updatedPerson);
    }

    public void delete(Integer id){
        personRepository.deleteById(id);
    }
    public Optional<Person> findByName(String name){
        return personRepository.findByName(name);
    }
    public Integer getIdByName(String name){
       Optional<Person> personOptional = findByName(name);
       if(personOptional.isPresent()){
           Person person = personOptional.get();
           return person.getId();
       }else {
           return null;
       }
    }
    public List<Book> getCurrentBooks(int id){
        Optional<Person> person = personRepository.findById(id);

        if(person.isPresent()) {
            List<Book> listOfBooks = person.get().getBooks();

            long duration = ((10 * 60 * 60 * 24) * 1000);
            for (Book book : listOfBooks){
                if (book.getOwnedAt()!=null){
                    if ((book.getOwnedAt().getTime() + duration) <= (Timestamp.from(Instant.now()).getTime()))
                        book.setExpired(true);
                }
            }
            return listOfBooks;
        }
        else
            return Collections.emptyList();
    }

}
