package cn.wenjig.crm.service;

import cn.wenjig.crm.data.domain.LogListDomain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Queue;

@Service
public interface SystemLogService {

    LogListDomain findAllByIndexPage(int start, int capacity);

    @Transactional
    void addLog(Queue queue);

}
