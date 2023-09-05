package com.example.server.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "users")
public class Users {


    @Id
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 15)
    private String mobilenummer;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    public Users(Long id, String username, String password, String mobilenummer, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mobilenummer = mobilenummer;
        this.email = email;
    }

    public Users() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMobilenummer() {
        return mobilenummer;
    }

    public void setMobilenummer(String mobilenummer) {
        this.mobilenummer = mobilenummer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(username, users.username) && Objects.equals(password, users.password) && Objects.equals(mobilenummer, users.mobilenummer) && Objects.equals(email, users.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, mobilenummer, email);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobilenummer='" + mobilenummer + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}