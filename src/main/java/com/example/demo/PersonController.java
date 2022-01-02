//package com.example.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PersonController implements CommandLineRunner{
//	
//	@Autowired
//	PersonRepository personRepo; 
//
//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("hello .... ");
//		Person person = new Person();
//		person.setFirstName("ajay");
//		person.setLastName("mittal");
//		person.setPersonId((long)3);
//		personRepo.save(person);
//		
//		
//		personRepo.findAll().forEach(per -> System.out.println(per.getFirstName()));
//		
//	}
//
//}
