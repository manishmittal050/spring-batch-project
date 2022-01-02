//package com.example.demo;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.item.ItemProcessor;
//
//public class PeopleItemProcessor implements ItemProcessor<Person, PersonAfterBatch>{
//	
//	 private static final Logger log = LoggerFactory.getLogger(PeopleItemProcessor.class);
//
//	@Override
//	public PersonAfterBatch process(Person person) throws Exception {
//		String firstName = person.getFirstName().toUpperCase();
//		String lastName = person.getLastName().toUpperCase();
//		long id = person.getPersonId();
//		
//		
//		PersonAfterBatch afterBatch = new PersonAfterBatch(firstName, lastName); 
//		
//		log.info("first name is "+ firstName);
//		
//		return afterBatch;
//	}
//
//}
