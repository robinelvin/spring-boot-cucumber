package com.robinelvin.sbc.models;

import org.neo4j.ogm.annotation.NodeEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@NodeEntity
public class Transaction extends BaseEntity {
    public ZonedDateTime postDate;
    public ZonedDateTime transactionDate;
    public String description;
    public TransactionCategory category;
    public BigDecimal amount;
    public Long transactionId;
    public TransactionType transactionType;

    //@Relationship(type = "INSTITUTION", direction = Relationship.DIRECTION)
    public Institution institution;

    public Transaction() { }

    public Transaction(ZonedDateTime postDate, ZonedDateTime transactionDate, String description, TransactionCategory category, BigDecimal amount, Institution institution, Long transactionId, TransactionType transactionType) {
        this.postDate = postDate;
        this.transactionDate = transactionDate;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.institution = institution;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
    }

}