package org.itstep.schooltimetable.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.itstep.schooltimetable.student.entity.Student;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class CustomUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    private String password;

    boolean accountNonExpired = true;

    boolean accountNonLocked = true;

    boolean credentialsNonExpired = true;

    boolean enabled = true;

    @ManyToMany(mappedBy = "users")
    private Set<CustomRole> authorities = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private Student student;

    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addRole(CustomRole... roles) {
        Arrays.stream(roles).forEach(role -> {
            authorities.add(role);
            role.getUsers().add(this);
        });
    }
}
