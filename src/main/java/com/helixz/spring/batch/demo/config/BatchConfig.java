package com.helixz.spring.batch.demo.config;

import com.helixz.spring.batch.demo.component.OrderFieldMapper;
import com.helixz.spring.batch.demo.entity.Order;
import com.helixz.spring.batch.demo.listener.CustomJobListener;
import com.helixz.spring.batch.demo.processor.OrderProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
 * @author Chamith
 */
@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private OrderFieldMapper fieldMapper;

    @Value("${app.csv.fileHeaders}")
    private String[] names;

    @Bean
    public FlatFileItemReader<Order> reader() {
        return new FlatFileItemReaderBuilder<Order>()
                .name(ORDER_ITEM_READER)
                .resource(new ClassPathResource("csv/orders.csv"))
                .linesToSkip(1)
                .delimited()
                .names(names)
                .lineMapper(lineMapper())
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Order>() {{
                    setTargetType(Order.class);
                }}).build();
    }
    private WritableResource outputResource = new FileSystemResource("output/outputData.csv");
    @Bean
    public FlatFileItemWriter<Order> writer()
    {
        //Create writer instance
        FlatFileItemWriter<Order> writer = new FlatFileItemWriter<>();

        //Set output file location
        writer.setResource(outputResource);

        //All job repetitions should "append" to same output file
        writer.setAppendAllowed(true);

        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<Order>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Order>() {
                    {
                        setNames(new String[] { "orderRef" });
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public LineMapper<Order> lineMapper() {

        final DefaultLineMapper<Order> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(names);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldMapper);

        return defaultLineMapper;
    }

    @Bean
    public OrderProcessor processor() {
        return new OrderProcessor();
    }



    @Bean
    public Step step(FlatFileItemWriter<Order> writer) {
        return stepBuilderFactory.get(BATCH_STEP)
                .<Order, Order> chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(CustomJobListener listener, Step step) {
        return jobBuilderFactory.get(ORDER_PROCESS_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }


}
