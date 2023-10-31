package com.helixz.spring.batch.demo.component;

import com.imsweb.x12.Loop;
import com.imsweb.x12.reader.X12Reader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import org.springframework.batch.item.ItemReader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

@Component
public class EDIReader implements ItemReader<Loop> {
    private Iterator<Loop> loops;
    public EDIReader() throws IOException {
        URL url = EDIReader.class.getResource("/x12_277CA_accepted.txt");
        X12Reader reader = new X12Reader(X12Reader.FileType.ANSI837_5010_X214, new File(url.getFile()));
         loops = reader.getLoops().iterator();
    }
    @Override
    public Loop read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(loops.hasNext())
            return loops.next();
        else
            return null;
    }
}
