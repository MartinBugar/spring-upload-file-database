package com.martyx.springuploadfiledatabase.repository;


import com.martyx.springuploadfiledatabase.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository <FileDB, String> {
}
