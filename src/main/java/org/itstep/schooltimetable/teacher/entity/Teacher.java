package org.itstep.schooltimetable.teacher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.itstep.schooltimetable.security.entity.CustomUser;

@Data
@Entity
@Table(name = "teachers")
@EqualsAndHashCode(exclude = "user")
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private CustomUser user;

    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PreRemove
    public void prepareToRemoveUser() {
        user.removeAllRoles();
    }
}
