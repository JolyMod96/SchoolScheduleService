package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateAdminCommand;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.security.repository.CustomRoleRepository;
import org.itstep.schooltimetable.security.repository.CustomUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CustomUserRepository customUserRepository;
    private final CustomRoleRepository customRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CustomUser save(CreateAdminCommand command) {
        var admin = new CustomUser(command.getLogin(), passwordEncoder.encode(command.getPassword()));
        var adminRole = customRoleRepository.findByAuthorityIsLike("%ADMIN");
        admin.addRole(adminRole);
        if (command.getIsAdminCreator()) {
            var adminCreatorRole = customRoleRepository.findByAuthorityIsLike("%ADMIN_CREATOR%");
            admin.addRole(adminCreatorRole);
        }
        return customUserRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public List<CustomUser> findAllAdmins() {
        return customUserRepository.findAdmins();
    }

    public Optional<CustomUser> findById(long id) {
        return customUserRepository.findAdminById(id);
    }

    @Transactional
    public void delete(CustomUser admin) {
        var adminRole = customRoleRepository.findByAuthorityIsLike("%ADMIN");
        if (admin.getAuthorities().contains(adminRole)) {
            customUserRepository.delete(admin);
        }
    }

    public CustomUser findByUsername(String username) {
        return customUserRepository.findByUsername(username).orElseThrow();
    }
}
