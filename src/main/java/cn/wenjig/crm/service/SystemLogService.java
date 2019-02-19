package cn.wenjig.crm.service;

import cn.wenjig.crm.data.domain.LogListDomain;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Queue;

@Service
public interface SystemLogService {

    LogListDomain findAllByIndexPage(int start, int capacity);

    void addLog(Queue queue);

}
