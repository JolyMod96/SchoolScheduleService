package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.EditTimetableCommand;
import org.itstep.schooltimetable.schedule.entity.Timetable;
import org.itstep.schooltimetable.schedule.repository.TimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;

    @Transactional(readOnly = true)
    public List<Timetable> findAllTimetables() {
        return timetableRepository.findAll();
    }

    public Optional<Timetable> findById(long id) {
        return timetableRepository.findById(id);
    }

    public void edit(long id, EditTimetableCommand command) {
        var timetable = timetableRepository.findById(id).orElseThrow();
        timetable.setTimeStart(Duration.parse(command.getTimeStart()));
        timetable.setTimeEnd(Duration.parse(command.getTimeEnd()));
        timetableRepository.save(timetable);
    }
}
