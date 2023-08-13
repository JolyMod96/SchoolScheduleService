package org.itstep.schooltimetable.admin.command;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateAdminCommand {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    private Boolean isAdminCreator;
}
