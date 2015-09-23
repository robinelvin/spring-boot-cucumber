package com.robinelvin.sbc.models;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class TransactionCategory extends BaseEntity {
    public String name;

    public TransactionCategory() { }

    public TransactionCategory(String name) {
        this.name = name;
    }
}
