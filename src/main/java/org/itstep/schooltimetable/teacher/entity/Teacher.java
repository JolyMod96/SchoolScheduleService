package org.itstep.schooltimetable.teacher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.subject.entity.Subject;

import java.util.*;

@Data
@Entity
@Table(name = "teachers")
@EqualsAndHashCode(exclude = {"user", "subjects"})
@ToString(exclude = {"user", "subjects"})
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

    @ManyToMany(mappedBy = "teachers")
    private Set<Subject> subjects = new HashSet<>();

    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PreRemove
    public void prepareToRemoveTeacherAndUser() {
        removeAllSubjects();
        user.removeAllRoles();
    }

    public void removeAllSubjects() {
        subjects.forEach(subject -> subject.getTeachers().remove(this));
        subjects.clear();
    }

    public void addSubjects(Subject... subjects) {
        Arrays.stream(subjects).forEach(subject -> {
            this.subjects.add(subject);
            subject.getTeachers().add(this);
        });
    }

    public List<Long> getSubjectsId() {
        var subjectsId = new ArrayList<Long>();
        subjects.forEach(subject -> {
            subjectsId.add(subject.getId());
        });
        return subjectsId;
    }
}
