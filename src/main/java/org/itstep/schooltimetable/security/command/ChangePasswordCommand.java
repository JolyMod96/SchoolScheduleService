package org.itstep.schooltimetable.security.command;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ChangePasswordCommand {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
