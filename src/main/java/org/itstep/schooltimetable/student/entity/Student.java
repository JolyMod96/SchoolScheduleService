package org.itstep.schooltimetable.student.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.itstep.schooltimetable.group.entity.Group;
import org.itstep.schooltimetable.security.entity.CustomUser;

@Data
@Entity
@Table(name = "students")
@EqualsAndHashCode(exclude = "user")
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private CustomUser user;

    @ManyToOne
    private Group group;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PreRemove
    public void prepareToRemoveStudentAndUser() {
        if (group != null) {
            removeFromGroup();
        }
        user.removeAllRoles();
    }

    public void removeFromGroup() {
        group.getStudents().remove(this);
        group = null;
    }
}
