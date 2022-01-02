package com.example.demo;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.demo.first.entity.Person;
import com.example.demo.first.repo.PersonRepository;

@Configuration
public class BatchConfiguration {


	@Autowired
	PersonRepository personRepo;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public JobLauncher jobLauncher;

	
	
	@Scheduled(cron = "*/5 * * * * *")
	public void perform() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, Exception {
		System.out.println("from cromn job schedule after every 5 seconds "+new Date());
		JobParametersBuilder paramBuilder = new JobParametersBuilder();
		paramBuilder.addDate("runTime", new Date());
		this.jobLauncher.run(personJob(), paramBuilder.toJobParameters());
	}


	@Bean
	public Step driveStep() {
		return this.stepBuilderFactory.get("driveStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				System.out.println(new Date().getTime()+" Step two is running");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}


	@Bean
	public Step packageitemStep() {
		return this.stepBuilderFactory.get("packageItemStep")
				.tasklet(new Tasklet() {

					@Override
					public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
						System.out.println(new Date().getTime()+" Step one is running");
						return RepeatStatus.FINISHED;
					}
				})
				.build();
	}
	
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(10);
		return executor;
	}



	@Bean
	public Step chunkStep() {
		return this.stepBuilderFactory.get("chunkStep")
				.<Person,Person>chunk(3)
				.reader(itemReader())
				.processor(compositeProcessor())
				.writer(new ItemWriter<Person>() {
					@Override
					public void write(List<? extends Person> items) throws Exception {
						System.out.println("items size is "+items.size());
						items.forEach(System.out::println);	
					}
				})
				.taskExecutor(taskExecutor())
				.build();
	}


	private ItemProcessor<? super Person, ? extends Person> compositeProcessor() {

		return new CompositeItemProcessorBuilder<Person,Person>().delegates(firstProcessor(),secondProcessor()).build();
	}


	private ItemProcessor<Person, Person> secondProcessor() {

		return new ItemProcessor<Person, Person>(){

			@Override
			public Person process(Person arg0) throws Exception {
				System.out.println("callin second");
				return arg0;
			}
			
		};
	}


	private ItemProcessor<Person, Person> firstProcessor() {
		return new ItemProcessor<Person, Person>(){

			@Override
			public Person process(Person arg0) throws Exception {
				System.out.println("callin first");
				return arg0;
			}
			
		};
	}


	/*new ItemProcessor<Person, Person>() {

					@Override
					public Person process(Person arg0) throws Exception {
						System.out.println("do nothing ... ");
						return arg0;
					}
				}
	}*/



	//	@Bean
	//	public ItemWriter<Person> itemWriter() {
	//		return new SimpleItemWriter();
	//	}


	//		new ItemWriter<Person>() {
	//			@Override
	//			public void write(List<? extends Person> items) throws Exception {
	//				System.out.println("items size is "+items.size());
	//				items.forEach(System.out::println);	
	//			}
	//		}




	@Bean
	public ItemReader<Person> itemReader() {
		// TODO Auto-generated method stub
		return new SimpleItemReader();
	}





	@Bean
	public Job personJob() throws Exception{
		return this.jobBuilderFactory.get("personJob"+System.nanoTime())
				.incrementer(new RunIdIncrementer())
				.flow(chunkStep())
				.end()
				.build();
	}





	@Bean
	public List<Person> reader() {
		List<Person> persons = personRepo.findAll();
		return persons;
	}


}