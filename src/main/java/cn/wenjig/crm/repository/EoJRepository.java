package cn.wenjig.crm.repository;

import cn.wenjig.crm.data.entity.EoJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface EoJRepository extends JpaRepository<EoJ, Long> {

    @Query(value = "select eoj.jobId from EoJ eoj where eoj.employeeId=?1")
    HashSet<Long> findJobByEmployeeId(long id);

}
