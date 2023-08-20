package org.itstep.schooltimetable.admin.command;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EditScheduleCommand {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Integer weeksRepeat;
    private List<Long> daysOfWeekId;
    private Long timetableId;
    private Long groupId;
    private Long subjectId;
    private Boolean isSubstituteTeacher;
    private Long teacherId;
}
