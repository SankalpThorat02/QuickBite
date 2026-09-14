package com.sankalp.quickbite.user.repository;

import com.sankalp.quickbite.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
