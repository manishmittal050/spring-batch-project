package com.example.demo;

import java.util.Iterator;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.first.entity.Person;
import com.example.demo.first.repo.PersonRepository;

public class SimpleItemReader implements ItemReader<Person> {


	@Autowired
	private PersonRepository personRepo;

	private Iterator<Person> iterator;


	boolean flag = true;

	
	 @BeforeStep
	    public void before(StepExecution stepExecution) {
		 this.iterator = this.personRepo.findAll().iterator();
	    }



	@Override
	public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		System.out.println("inside read ......."+ Thread.currentThread().getId());
//		if(flag) {
//			flag = false;
//			//			iterator.forEachRemaining(System.out::println);
			return iterator.hasNext()?iterator.next():null;
//		}

//		return null;
	}

}
