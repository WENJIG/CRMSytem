package cn.wenjig.crm.service.impl;

import cn.wenjig.crm.data.entity.JobInfo;
import cn.wenjig.crm.repository.EoJRepository;
import cn.wenjig.crm.repository.JobInfoRepository;
import cn.wenjig.crm.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    private final JobInfoRepository jobInfoRepository;
    private final EoJRepository eoJRepository;

    @Autowired
    public JobInfoServiceImpl(JobInfoRepository jobInfoRepository, EoJRepository eoJRepository) {
        this.jobInfoRepository = jobInfoRepository;
        this.eoJRepository = eoJRepository;
    }

    @Override
    public List<JobInfo> findAllJobByEmployeeId(long id) {
        HashSet<Long> jobIds = eoJRepository.findJobByEmployeeId(id);
        return (List<JobInfo>) jobInfoRepository.findAllById(jobIds);
    }

}

