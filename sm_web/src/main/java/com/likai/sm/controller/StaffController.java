package com.likai.sm.controller;

import com.likai.sm.entity.Department;
import com.likai.sm.entity.Staff;
import com.likai.sm.service.DepartmentService;
import com.likai.sm.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller("staffController")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private DepartmentService departmentService;
    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Staff> list=staffService.getAll();
        request.setAttribute("list",list);
        request.getRequestDispatcher("../staff_list.jsp").forward(request,response);
    }

    public void toEdit(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Integer id=Integer.parseInt(request.getParameter("id"));
        Staff staff=staffService.get(id);
        List<Department> list=departmentService.getAll();
        request.setAttribute("staff",staff);
        request.setAttribute("departments",list);
        request.getRequestDispatcher("../staff_edit.jsp").forward(request,response);
    }

    public void edit(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Integer id=Integer.parseInt(request.getParameter("id"));
        String account=request.getParameter("account");
        Integer did=Integer.parseInt(request.getParameter("did"));
        String name=request.getParameter("name");
        String sex=request.getParameter("sex");
        String idNumber=request.getParameter("idNumber");
        String date=request.getParameter("bornDate");
        Date bornDate=null;
        try {
            bornDate=new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String info=request.getParameter("info");

        Staff staff=staffService.get(id);
        staff.setAccount(account);
        staff.setBornDate(bornDate);
        staff.setDid(did);
        staff.setInfo(info);
        staff.setIdNumber(idNumber);
        staff.setName(name);
        staff.setSex(sex);
        staffService.edit(staff);

        response.sendRedirect("list.do");

    }
    public void toAdd(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        List<Department> departments=departmentService.getAll();
        request.setAttribute("departments",departments);
        request.getRequestDispatcher("../staff_add.jsp").forward(request,response);
    }
    public void add(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String account=request.getParameter("account");
        Integer did=Integer.parseInt(request.getParameter("did"));
        String name=request.getParameter("name");
        String sex=request.getParameter("sex");
        String idNumber=request.getParameter("idNumber");
        Date bornDate=null;
        try {
            bornDate=new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("bornDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String info=request.getParameter("info");

        Staff staff=new Staff();
        staff.setAccount(account);
        staff.setDid(did);
        staff.setName(name);
        staff.setSex(sex);
        staff.setIdNumber(idNumber);
        staff.setBornDate(bornDate);
        staff.setInfo(info);
        staffService.add(staff);

        response.sendRedirect("list.do");

    }
    public void remove(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Integer id=Integer.parseInt(request.getParameter("id"));
        staffService.remove(id);
        response.sendRedirect("list.do");
    }
    public void detail(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Integer id=Integer.parseInt(request.getParameter("id"));
        Staff staff=staffService.get(id);
        request.setAttribute("staff",staff);
        request.getRequestDispatcher("../staff_detail.jsp").forward(request,response);
    }
}
