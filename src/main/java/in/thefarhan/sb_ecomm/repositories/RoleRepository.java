package in.thefarhan.sb_ecomm.repositories;

import in.thefarhan.sb_ecomm.model.AppRole;
import in.thefarhan.sb_ecomm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
