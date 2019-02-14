package cn.wenjig.crm.repository;

import cn.wenjig.crm.data.entity.JoP;
import cn.wenjig.crm.data.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface JoPRepository extends JpaRepository<JoP, Long> {

    @Query(value = "select jop.permissionId from JoP jop where jop.jobId=?1")
    HashSet<Long> findByJobId(long id);

}
