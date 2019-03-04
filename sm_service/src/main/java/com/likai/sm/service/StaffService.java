package com.likai.sm.service;

import com.likai.sm.entity.Staff;
import org.springframework.stereotype.Service;

import java.util.List;

public interface StaffService {
    void add(Staff staff);
    void remove(Integer id);
    void edit(Staff staff);
    Staff get(Integer id);
    List<Staff> getAll();
}
