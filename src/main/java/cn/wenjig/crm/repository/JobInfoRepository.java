package cn.wenjig.crm.repository;

import cn.wenjig.crm.data.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, Long> {

    JobInfo findById(long id);

}
