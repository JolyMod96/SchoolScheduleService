package org.itstep.schooltimetable.group.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itstep.schooltimetable.student.entity.Student;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "groups")
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<Student> students = new HashSet<>();

    public Group(String name) {
        this.name = name;
    }

    @PreRemove
    public void prepareToRemoveGroup() {
        removeAllStudents();
    }

    public void removeAllStudents() {
        students.forEach(student -> student.setGroup(null));
        students.clear();
    }

    public void addRole(Student... students) {
        Arrays.stream(students).forEach(student -> {
            this.students.add(student);
            student.setGroup(this);
        });
    }
}
