package com.vamospassar.respostabot.model.jpa;

import com.vamospassar.respostabot.enums.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "tb_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String number_cell;

    @Column(unique = true)
    private String email;

    private Long quantityQuestionsRequest;

    private String password;

    private boolean isActive;

    private LocalDateTime finaFreelTime;

    @OneToMany(mappedBy = "user")
    private List<Question> questions;


    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public void setFinaFreelTime(LocalDateTime finaFreelTime){
        this.finaFreelTime = finaFreelTime;
    }

    @PrePersist
    protected void onCreaetAt() {
        createdAt = LocalDateTime.now();
        finaFreelTime = LocalDateTime.now().plusDays(7);
        quantityQuestionsRequest = 0L;
    }

    @PreUpdate
    protected void onUpdateAt() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber_cell(){
        return this.number_cell;
    }

    public Role getRole(){
        return  this.role;
    }
    public String getName(){
        return  this.name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_COSTUMER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_COSTUMER"));
        }
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean value) {
        this.isActive = value;
    }

    public LocalDateTime getFinalFreeTime() {
        return finaFreelTime;
    }


    private User(String email, String password, boolean isActive, boolean isFreeTime, Role role, String name, String number_cell) {
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
        this.name = name;
        this.number_cell = number_cell;
    }

    public User() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Long getQuantityQuestionsRequest() {
        return quantityQuestionsRequest;
    }

    public void setQuantityQuestionsRequest(Long quantityQuestionsRequest) {
        this.quantityQuestionsRequest = quantityQuestionsRequest;
    }

    public static class Builder {
        private String email;
        private String password;
        private boolean isActive;
        private boolean isFreeTime;
        private Role role;
        private String name;
        private String number_cell;


        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder isFreeTime(boolean isFreeTime) {
            this.isFreeTime = isFreeTime;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder cell(String number_cell) {
            this.number_cell = number_cell;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public User build() {
            return new User(email, password, isActive, isFreeTime, role, name, number_cell);
        }


    }


}
