package com.uas.api.repositories.auth;


import com.uas.api.models.auth.ERole;
import com.uas.api.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by name.
     * @param name name
     * @return optional containing name and user role or null.
     */
    Optional<Role> findByName(ERole name);
}
