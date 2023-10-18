package com.helixz.spring.batch.demo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Chamith
 */
@Data
public class Order {
    private Long id;

    private String orderRef;

    private BigDecimal amount;

    private LocalDateTime orderDate;

    private String note;

    private Date tempOrderDate;

}
