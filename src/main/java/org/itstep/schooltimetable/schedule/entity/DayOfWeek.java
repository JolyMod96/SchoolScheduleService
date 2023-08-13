package org.itstep.schooltimetable.schedule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "days_of_week")
@Data
@EqualsAndHashCode(exclude = "schedules")
@ToString(exclude = "schedules")
@NoArgsConstructor
public class DayOfWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "days_of_week__schedules",
            joinColumns = @JoinColumn(name = "days_of_week_id"),
            inverseJoinColumns = @JoinColumn(name = "schedules_id")
    )
    private Set<Schedule> schedules = new HashSet<>();

    public DayOfWeek(String name) {
        this.name = name;
    }
}
