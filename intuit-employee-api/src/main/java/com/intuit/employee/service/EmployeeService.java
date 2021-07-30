package com.intuit.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.intuit.employee.entity.Employee;
import com.intuit.employee.repository.EmployeeRepository;

@Service
public class EmployeeService 
{
    @Autowired
    EmployeeRepository repository;
     
    public List<Employee> getAllEmployees(Integer pageNo, Integer pageSize)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize,Sort.by("id"));
 
        Page<Employee> pagedResult = repository.findAll(paging);
         
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return null;
        }
    }
    
}
