package com.likai.sm.global;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DispatcherServlet extends GenericServlet {
    private ApplicationContext applicationContext;
    public void init() throws ServletException {
        super.init();
        applicationContext=new ClassPathXmlApplicationContext("spring.xml");
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;

        /**
         *       请求名和bean的命名规范统一，业界流行写法
         *      /staff/add.do    /login.do
         *      staffController
         *      public void add(HttpServletRequest request, HttpServletResponse response){}
         */
        System.out.println(request.getServletPath());

        String path=request.getServletPath().substring(1);
        String beanName=null;
        String methodName=null;
        int index=path.indexOf('/');
        if(index!=-1){
            beanName=path.substring(0,index)+"Controller";
            methodName=path.substring(index+1,path.indexOf(".do"));
        }else {
            beanName="selfController";
            methodName=path.substring(0,path.indexOf(".do"));
        }
        System.out.println(methodName);
        Object object=applicationContext.getBean(beanName);
        try {
            Method method = object.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            System.out.println(method);
            System.out.println(object);
            method.invoke(object,request,response);
            System.out.println("invoke成功");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
