package org.itstep.schooltimetable.admin.command;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<Long> subjectsId = new ArrayList<>();
}
