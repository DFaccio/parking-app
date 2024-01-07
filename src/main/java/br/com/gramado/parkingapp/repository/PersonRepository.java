package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    Optional<Person> findPersonByDocumentEquals(String document);
}
