package com.code.wizards.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="users")
@EntityListeners(value=UserListener.class)
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", columnDefinition = "VARBINARY(255)", unique = true, updatable = false, nullable = false)
    private UUID id;

    @Email(message = "Email is not valid", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank(message = "Email Is Required")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Username Is Required")
    @Column(unique = true)
    private String username;
    
    @NotBlank(message = "Password Is Required")
    private String password;

    private String firstname;
    private String lastname;
    private String course;
    private String branch;
    private Date dob;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public User(){}

    public User(@NotBlank(message = "Email Is Required") String email,
            @NotBlank(message = "Username Is Required") String username,
            @NotBlank(message = "Password Is Required") String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(@NotBlank(message = "Email Is Required") String email,
            @NotBlank(message = "Username Is Required") String username,
            @NotBlank(message = "Password Is Required") String password, String firstname, String lastname,
            String course, String branch, Date dob, Timestamp createdAt, Timestamp updatedAt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.course = course;
        this.branch = branch;
        this.dob = dob;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User [branch=" + branch + ", course=" + course + ", createdAt=" + createdAt + ", dob=" + dob
                + ", email=" + email + ", firstname=" + firstname + ", id=" + id + ", lastname=" + lastname
                + ", password=" + password + ", updatedAt=" + updatedAt + ", username=" + username + "]";
    }

}

class UserListener{
    @PrePersist
    public void setUUID(User user){
        user.setId(java.util.UUID.randomUUID());
    }
}