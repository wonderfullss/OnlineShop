package com.example.onlineshop.Entity;

import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;


    @NotBlank
    private String password;

    @Min(0)
    private Double balance;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Notification> notification;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Organization> organizations;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<HistoryOrders> historyOrders;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String email, String password, Double balance, Role role) {
        this.username = username;
        this.email = email;
        this.balance = balance;
        this.role = role;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
}
