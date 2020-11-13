package com.martyx.springuploadfiledatabase.service;


import com.martyx.springuploadfiledatabase.model.FileDB;
import com.martyx.springuploadfiledatabase.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOError;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile multipartFile) throws IOException{
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        FileDB fileDB = new FileDB(filename, multipartFile.getContentType(),multipartFile.getBytes());

        return fileDBRepository.save(fileDB); // JPA repository mi to ulozi do MySql
    }

    public FileDB getFile (String id){
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles (){
        return fileDBRepository.findAll().stream();
    }
}
