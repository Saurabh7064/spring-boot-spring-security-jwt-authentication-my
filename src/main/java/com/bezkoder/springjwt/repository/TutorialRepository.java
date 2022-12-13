package com.bezkoder.springjwt.repository;
import java.util.List;

import com.bezkoder.springjwt.models.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);
}
