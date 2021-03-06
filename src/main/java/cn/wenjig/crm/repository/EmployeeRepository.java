package cn.wenjig.crm.repository;

import cn.wenjig.crm.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "select e from Employee e where e.email=?1")
    Employee findByName(String name);

    Employee findById(long id);

}
