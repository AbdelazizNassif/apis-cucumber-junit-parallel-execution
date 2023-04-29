package models;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({"name", "gender" , "email" , "status"})
public class User {

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("email")
    private String email;
    @JsonProperty("status")
    private String status;

    public User() {
    }

    public User( String name, String gender, String email, String status) {
        setName(name);
        setGender(gender);
        setEmail(email);
        setStatus(status);
    }
    public User(int id, String gender, String name, String email, String status) {
        setId(id);
        setName(name);
        setGender(gender);
        setEmail(email);
        setStatus(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
