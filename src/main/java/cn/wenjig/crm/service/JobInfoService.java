package cn.wenjig.crm.service;

import cn.wenjig.crm.data.entity.JobInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface JobInfoService {

    List<JobInfo> findAllJobByEmployeeId(long id);

}
