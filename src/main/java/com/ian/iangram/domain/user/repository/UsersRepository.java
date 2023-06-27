package com.ian.iangram.domain.user.repository;

import com.ian.iangram.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
