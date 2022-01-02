package com.example.demo.secondRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.secondEntity.Individual;


@Repository
public interface SecondRepository extends JpaRepository<Individual, Long>{

}
