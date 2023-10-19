package com.helixz.spring.batch.demo.processor;

import com.helixz.spring.batch.demo.entity.Order;
import com.imsweb.x12.Loop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;


/**
 * @author Chamith
 */
@Slf4j
@Component
public class EDIProcessor implements ItemProcessor<Loop, Loop> {
    @Override
    public Loop process(Loop loop) throws Exception {
        loop.getLoop("GS_LOOP").getLoop("ST_LOOP").getLoop("DETAIL")
                .getLoop("2000A")
                .getLoop("2000B")
                .getLoop("2000C")
                .getLoop("2000D")
                .getLoop("2200D")
                .getSegment("REF")
                .getElement("REF02").setValue("111000111");
        return loop;
    }
}
