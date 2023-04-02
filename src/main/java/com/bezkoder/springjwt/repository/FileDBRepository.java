package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.FileDB;
import com.bezkoder.springjwt.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
//    List<Group> findByCreatedBy(String createdBy);
    List<FileDB> findByName(String name);

}

