package com.simplesocialnetwork.repo;

import com.simplesocialnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<User, String> {
}
