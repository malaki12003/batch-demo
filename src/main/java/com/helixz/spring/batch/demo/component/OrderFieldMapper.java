package com.helixz.spring.batch.demo.component;

import com.helixz.spring.batch.demo.entity.Order;
import com.imsweb.x12.LineBreak;
import com.imsweb.x12.reader.X12Reader;
import com.imsweb.x12.writer.X12Writer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Chamith
 */
@Component
public class OrderFieldMapper implements FieldSetMapper<Order> {

    @Override
    public Order mapFieldSet(FieldSet fieldSet) {
        Order order = new Order();
        order.setOrderRef(fieldSet.readString("order_ref"));
        order.setAmount(fieldSet.readBigDecimal("amount"));
        order.setNote(fieldSet.readString("note"));
        order.setTempOrderDate(fieldSet.readDate("order_date"));
        return order;
    }
}
