package com.likai.sm.service;

import com.likai.sm.entity.Staff;

public interface SelfService {
    Staff login(String account,String password);
    void changePassword(Integer id,String password);
}
