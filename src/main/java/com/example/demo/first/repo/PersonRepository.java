package com.example.demo.first.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.first.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	

}
