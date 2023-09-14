package org.itstep.schooltimetable.admin.command;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EditScheduleCommand {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Integer weeksRepeat = 1;
    private List<Long> daysOfWeekId;
    private Long timetableId;
    private Long groupId;
    private Long subjectId;
    private boolean isSubstituteTeacher;
    private Long teacherId;
}
