package com.uas.api.repositories.auth;


import com.uas.api.models.auth.ERole;
import com.uas.api.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by name.
     * @param name name
     * @return optional containing name and user role or null.
     */
    Optional<Role> findRoleByRoleName(ERole name);

}
