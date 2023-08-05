package org.itstep.schooltimetable.schedule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itstep.schooltimetable.converter.DurationConverter;

import java.time.Duration;

@Entity
@Table(name = "timetables")
@Data
@NoArgsConstructor
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = DurationConverter.class)
    private Duration start;
    @Convert(converter = DurationConverter.class)
    private Duration end;
    @NotBlank
    @Column(unique = true)
    private String name;
}
