package cn.wenjig.crm.service;

import cn.wenjig.crm.data.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    Employee findByName(String name);

    List<Employee> findAll();

    Employee addOne(Employee employee);

}
