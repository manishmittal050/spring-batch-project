package com.example.demo.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class BatchDataSourceConfig {

	@Bean(name="batchDataSource")
	@Primary
	@ConfigurationProperties(prefix = "first.datasource")
	public DataSource batchDataSource(){
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "batchEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("batchDataSource") DataSource batchDataSource){
		return builder
				.dataSource(batchDataSource)
				.build();
	}

	@Primary
	@Bean(name = "batchTransactionManager")
	public PlatformTransactionManager appTransactionManager(@Qualifier("batchEntityManagerFactory") EntityManagerFactory
			batchEntityManagerFactory) {

		return new JpaTransactionManager(batchEntityManagerFactory);
	}

	@Bean
	public BatchConfigurer configurer(@Qualifier("batchDataSource") DataSource batchDataSource) {

		return new DefaultBatchConfigurer(batchDataSource);
	}

}
