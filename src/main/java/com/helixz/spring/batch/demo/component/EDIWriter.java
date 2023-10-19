package com.helixz.spring.batch.demo.component;

import com.helixz.spring.batch.demo.Main2;
import com.imsweb.x12.LineBreak;
import com.imsweb.x12.Loop;
import com.imsweb.x12.Separators;
import com.imsweb.x12.reader.X12Reader;
import com.imsweb.x12.writer.X12Writer;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class EDIWriter implements ItemWriter<Loop> {
    private Iterator<Loop> loops;
    Separators separators = new Separators();
    public EDIWriter() throws IOException {
        separators.setLineBreak(LineBreak.CRLF);
    }

    @Override
    public void write(List<? extends Loop> list) throws Exception {
        X12Writer writer = new X12Writer(X12Reader.FileType.ANSI837_5010_X214, (List<Loop>) list, list.size()>0?list.get(0).getSeparators():separators);
        System.out.println( writer.toX12String(LineBreak.CRLF).trim());
    }
}
