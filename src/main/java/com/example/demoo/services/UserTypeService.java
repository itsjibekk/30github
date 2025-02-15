package com.example.demoo.services;

import com.example.demoo.models.UserType;
import com.example.demoo.repo.UserTypeRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserTypeService {

    @Autowired
    private UserTypeRepo userTypeRepo;

    public List<UserType> findAll() {
       return userTypeRepo.findAll();
    }
}
