package org.itstep.schooltimetable.group.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itstep.schooltimetable.schedule.entity.Schedule;
import org.itstep.schooltimetable.student.entity.Student;
import org.itstep.schooltimetable.subject.entity.Subject;

import java.util.*;

@Data
@Entity
@Table(name = "groups")
@EqualsAndHashCode(exclude = {"students", "subjects", "schedules"})
@ToString(exclude = {"students", "subjects", "schedules"})
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "groups")
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<Schedule> schedules = new HashSet<>();

    public Group(String name) {
        this.name = name;
    }

    @PreRemove
    public void prepareToRemoveGroup() {
        removeAllSubjects();
        removeAllStudents();
    }

    public void removeAllStudents() {
        students.forEach(student -> student.setGroup(null));
        students.clear();
    }

    public void addStudents(Student... students) {
        Arrays.stream(students).forEach(student -> {
            this.students.add(student);
            student.setGroup(this);
        });
    }

    public void removeAllSubjects() {
        subjects.forEach(subject -> subject.getGroups().remove(this));
        subjects.clear();
    }

    public void addSubjects(Subject... subjects) {
        Arrays.stream(subjects).forEach(subject -> {
            this.subjects.add(subject);
            subject.getGroups().add(this);
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
