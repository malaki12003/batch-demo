package com.helixz.spring.batch.demo.config;

import com.helixz.spring.batch.demo.component.EDIReader;
import com.helixz.spring.batch.demo.component.EDIWriter;
import com.helixz.spring.batch.demo.component.OrderFieldMapper;
import com.helixz.spring.batch.demo.entity.Order;
import com.helixz.spring.batch.demo.listener.CustomJobListener;
import com.helixz.spring.batch.demo.processor.EDIProcessor;
import com.helixz.spring.batch.demo.processor.OrderProcessor;
import com.imsweb.x12.Loop;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;

import static com.helixz.spring.batch.demo.constant.BatchJobConstant.*;

/**
 * @author Mahdi
 */
@Configuration
public class EDIBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;



    @Value("${app.csv.fileHeaders}")
    private String[] names;



    @Bean
    public Step ediStep(EDIWriter ediWriter, EDIReader reader, EDIProcessor processor) {
        return stepBuilderFactory.get(BATCH_STEP)
                .<Loop, Loop> chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(ediWriter)
                .build();
    }

    @Bean("EDI_PROCESS_JOB")
    public Job job(CustomJobListener listener, Step ediStep) {
        return jobBuilderFactory.get(EDI_PROCESS_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(ediStep)
                .end()
                .build();
    }
}
