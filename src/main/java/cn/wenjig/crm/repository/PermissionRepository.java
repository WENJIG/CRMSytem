package cn.wenjig.crm.repository;

import cn.wenjig.crm.data.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(nativeQuery = true, value = "select permission_id from job_permission where job_id=?")
    Set<Integer> findByJobId(long id);

    Permission findById(long id);

}
