package org.itstep.schooltimetable.admin.command;

import lombok.*;
import org.itstep.schooltimetable.group.entity.Group;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class SelectGroupCommand {
    private List<Group> groups;
    private Group selectedGroup;
}
