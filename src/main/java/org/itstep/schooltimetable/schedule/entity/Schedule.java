package org.itstep.schooltimetable.schedule.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itstep.schooltimetable.group.entity.Group;
import org.itstep.schooltimetable.subject.entity.Subject;
import org.itstep.schooltimetable.teacher.entity.Teacher;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Integer weeksRepeat;

    @ManyToMany(mappedBy = "schedules")
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    @ManyToOne
    private Timetable timetable;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Group group;

    @ManyToOne
    private Teacher teacher;

    public Schedule(LocalDate dateStart, LocalDate dateEnd, Integer weeksRepeat) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weeksRepeat = weeksRepeat;
    }

    public void removeAllDaysOfWeek() {
        daysOfWeek.forEach(dayOfWeek -> dayOfWeek.getSchedules().remove(this));
        daysOfWeek.clear();
    }

    public void addDayOfWeek(DayOfWeek... daysOfWeek) {
        Arrays.stream(daysOfWeek).forEach(dayOfWeek -> {
            this.daysOfWeek.add(dayOfWeek);
            dayOfWeek.getSchedules().add(this);
        });
    }

    public void removeTeacher() {
        teacher.getSchedules().remove(this);
        teacher = null;
    }

    public void removeGroup() {
        group.getSchedules().remove(this);
        group = null;
    }

    public void removeSubject() {
        subject.getSchedules().remove(this);
        subject = null;
    }

    public void removeTimetable() {
        timetable.getSchedules().remove(this);
        timetable = null;
    }

    public void removeAll() {
        removeAllDaysOfWeek();
        removeTimetable();
        removeSubject();
        removeGroup();
        removeTeacher();
    }

    public List<Long> getDaysOfWeekId() {
        var daysOfWeekId = new ArrayList<Long>();
        daysOfWeek.forEach(dayOfWeek -> daysOfWeekId.add(dayOfWeek.getId()));
        return daysOfWeekId;
    }

    @PreRemove
    public void prepareToRemoveSubject() {
        removeAll();
    }

}
