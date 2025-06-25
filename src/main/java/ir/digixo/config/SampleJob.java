package ir.digixo.config;

import ir.digixo.model.StudentJdbc;

import ir.digixo.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class SampleJob {

    final private static int BATCH_SIZE = 3;
    final private JobRepository jobRepository;
    final private PlatformTransactionManager batchTransactionManager;
    final private DataSource dataSource;
    final private FirstItemWriter firstItemWriter;

    public SampleJob(JobRepository jobRepository, PlatformTransactionManager batchTransactionManager, @Qualifier("datasource2") DataSource dataSource, FirstItemWriter firstItemWriter) {
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
        this.dataSource = dataSource;
        this.firstItemWriter = firstItemWriter;
    }

    @Bean
    public Job firstJob() {
        return new JobBuilder("First Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    @Bean
    public Step firstChunkStep() {
        return new StepBuilder("First Chunk Step", jobRepository)
                .<StudentJdbc, StudentJdbc>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(jdbcCursorItemReader())
                .writer(firstItemWriter)
                .build();
    }

    public JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader() {
        JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<>();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("select id,first_name as firstName,last_name as lastName,email from students");
        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<StudentJdbc>() {
            {
                setMappedClass(StudentJdbc.class);
            }
        });
        return jdbcCursorItemReader;
    }

}
