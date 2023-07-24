package org.itstep.schooltimetable.admin.command;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EditTeacherCommand {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
