package com.robinelvin.sbc.models;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Account extends BaseEntity {
    public Institution institution;
    @Index private Long accountId;
    private String name;

    public Account() {}

    public Account(Institution institution, Long accountId, String name) {
        this.institution = institution;
        this.accountId = accountId;
        this.name = name;
    }

//    public Institution getInstitution() {
//        return this.institution;
//    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

//    public void setInstitution(Institution institution) {
//        this.institution = institution;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Relationship(type = "HAS_TXN", direction = Relationship.UNDIRECTED)
    private Set<Transaction> transactions;

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new HashSet<Transaction>();
        }
        transactions.add(transaction);
    }
}
