package com.example.demo;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.example.demo.first.entity.Person;

public class SimpleItemWriter implements ItemWriter<Person> {

	@Override
	public void write(List<? extends Person> arg0) throws Exception {
		System.out.println("Inside writer");

	}

}
