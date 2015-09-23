package com.robinelvin.sbc.models;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Robin Elvin
 */
@NodeEntity
public class User extends BaseEntity {

    public static final String HAS_ACCOUNT = "HAS_ACCOUNT";
    public static final String MEMBER_OF_INSTITUTION = "MEMBER_OF_INSTITUTION";

    @Index(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private Roles[] roles;

    @Transient
    private PasswordEncoder passwordEncoder;

    public User() {}

    public User(String username, String firstName, String lastName, String password, PasswordEncoder passwordEncoder, Roles... roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordEncoder = passwordEncoder;
        this.roles = roles;
        this.password = encodePassword(password);
    }

    // Getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public Roles[] getRoles() {
        return roles;
    }

    // Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
        //this.password = encodePassword(password);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Roles
    public enum Roles implements GrantedAuthority {
        ROLE_USER, ROLE_ADMIN;

        @Override
        public String getAuthority() {
            return name();
        }
    }

    public void setRoles(Roles[] roles) {
        this.roles = roles;
    }


    // Bank accounts
    @Relationship(type=MEMBER_OF_INSTITUTION, direction = Relationship.OUTGOING)
    private Set<HasAccountsWith> hasAccountsWith;

    public Set<HasAccountsWith> getHasAccountsWith() {
        if (hasAccountsWith == null) {
            hasAccountsWith = new HashSet<HasAccountsWith>();
        }
        return hasAccountsWith;
    }

    public void addInstitution(Institution institution, Long memSiteAccId, String refreshStatus) {
        HasAccountsWith hasAccountsWith = new HasAccountsWith(this, institution, memSiteAccId, refreshStatus);
        getHasAccountsWith().add(hasAccountsWith);
    }

    @Relationship(type=HAS_ACCOUNT, direction = Relationship.OUTGOING)
    private Set<Account> accounts;

    public Set<Account> getAccounts() {
        if (this.accounts == null) {
            this.accounts = new HashSet<Account>();
        }
        return this.accounts;
    }

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new HashSet<Account>();
        }
        accounts.add(account);
    }

    // Password
    private String encodePassword(String password) {
        //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder != null) {
            return passwordEncoder.encode(password);
        } else {
            return password;
        }
    }

    public void updatePassword(String old, String newPass1, String newPass2) {
        if (!password.equals(encodePassword(old))) throw new IllegalArgumentException("Existing Password invalid");
        if (!newPass1.equals(newPass2)) throw new IllegalArgumentException("New Passwords don't match");
        this.password = encodePassword(newPass1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        if (nodeId == null) return super.equals(o);
        return nodeId.equals(user.nodeId);

    }
}