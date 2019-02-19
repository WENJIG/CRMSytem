package cn.wenjig.crm.repository;

import cn.wenjig.crm.data.entity.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface SystemLogRepository extends JpaRepository<LogInfo, Long> {

    @Query(nativeQuery = true,
            value = "select * from crm.log_info where id >= (select id from crm.log_info order by id limit ?1,1) limit ?2")
    LinkedList<LogInfo> findAllByIndexPage(int start, int capacity);

    @Query(nativeQuery = true,
            value = "select count(id) from log_info")
    Long countById();

}
