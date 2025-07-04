package com.example._Board.user.repository;

import com.example._Board.user.domain.DeleteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteUserRepository extends JpaRepository<DeleteUser, Long> {
}
