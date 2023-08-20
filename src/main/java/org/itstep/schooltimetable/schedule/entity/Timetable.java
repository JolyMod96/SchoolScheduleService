package org.itstep.schooltimetable.schedule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itstep.schooltimetable.converter.DurationConverter;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "timetables")
@EqualsAndHashCode(exclude = "schedules")
@ToString(exclude = "schedules")
@NoArgsConstructor
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = DurationConverter.class)
    private Duration timeStart;
    @Convert(converter = DurationConverter.class)
    private Duration timeEnd;
    @NotBlank
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "timetable")
    private Set<Schedule> schedules = new HashSet<>();

    public Timetable(Duration timeStart, Duration timeEnd, String name) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.name = name;
    }

    private static String convert(Duration duration) {
        int minutesPart = duration.toMinutesPart();
        long hours = duration.toHours();
        return zeroIfNeed(hours) + hours + ":" + zeroIfNeed(minutesPart) + minutesPart;
    }

    private static String zeroIfNeed(long part) {
        return part < 10 ? "0" : "";
    }

    public String getTimeStartHM() {
        return convert(timeStart);
    }

    public String getTimeEndHM() {
        return convert(timeEnd);
    }
}
