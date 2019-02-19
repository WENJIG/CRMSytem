package cn.wenjig.crm.service.impl;

import cn.wenjig.crm.data.domain.LogListDomain;
import cn.wenjig.crm.repository.SystemLogRepository;
import cn.wenjig.crm.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Queue;

@Service
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogRepository systemLogRepository;

    @Autowired
    public SystemLogServiceImpl(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    @Override
    public LogListDomain findAllByIndexPage(int start, int capacity) {
        LogListDomain logListDomain = new LogListDomain();
        logListDomain.setLogInfos(systemLogRepository.findAllByIndexPage(start, capacity));
        logListDomain.setCount(systemLogRepository.countById());
        return logListDomain;
    }

    @Override
    public void addLog(Queue queue) {
        systemLogRepository.saveAll(queue);
    }

}
