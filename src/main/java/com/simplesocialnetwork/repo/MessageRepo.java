package com.simplesocialnetwork.repo;

import com.simplesocialnetwork.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ivan Gordeev 21.05.2023
 */

public interface MessageRepo extends JpaRepository<Message, Long> {
}
