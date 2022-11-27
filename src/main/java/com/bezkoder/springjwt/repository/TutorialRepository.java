package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
}
