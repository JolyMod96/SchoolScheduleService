package org.itstep.schooltimetable.admin.command;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EditTimetableCommand {
    @NotBlank
    private String timeStart;
    @NotBlank
    private String timeEnd;
}
