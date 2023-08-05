package org.itstep.schooltimetable.subject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itstep.schooltimetable.group.entity.Group;
import org.itstep.schooltimetable.teacher.entity.Teacher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects")
@Data
//@EqualsAndHashCode(exclude = {"groups", "teachers"})
//@ToString(exclude = {"groups", "teachers"})
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "subjects_groups",
            joinColumns = @JoinColumn(name = "subjects_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    private Set<Group> groups = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "subjects_teachers",
            joinColumns = @JoinColumn(name = "subjects_id"),
            inverseJoinColumns = @JoinColumn(name = "teachers_id")
    )
    private Set<Teacher> teachers = new HashSet<>();

    public Subject(String name) {
        this.name = name;
    }

    public void removeAllGroups() {
        groups.forEach(group -> group.getSubjects().remove(this));
        groups.clear();
    }

    public void addGroup(Group... groups) {
        Arrays.stream(groups).forEach(group -> {
            this.groups.add(group);
            group.getSubjects().add(this);
        });
    }

    public void removeAllTeachers() {
        teachers.forEach(teacher -> teacher.getSubjects().remove(this));
        teachers.clear();
    }

    public void addTeacher(Teacher... teachers) {
        Arrays.stream(teachers).forEach(teacher -> {
            this.teachers.add(teacher);
            teacher.getSubjects().add(this);
        });
    }

    @PreRemove
    public void prepareToRemoveSubject() {
        removeAllGroups();
        removeAllTeachers();
    }
}
