package cn.wenjig.crm.service.impl;

import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.repository.EmployeeRepository;
import cn.wenjig.crm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee findByName(String name) {
        return employeeRepository.findByName(name);
    }

}
