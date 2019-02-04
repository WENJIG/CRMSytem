package cn.wenjig.crm.repository;

import cn.wenjig.crm.data.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, Long> {

    @Query(nativeQuery = true, value = "select job_id from employee_job where employee_id=?1")
    Set<Integer> findJobByEmployeeId(long id);

    JobInfo findById(long id);
}
