package cn.wenjig.crm.service;

import cn.wenjig.crm.data.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

    Employee findByName(String name);

}
