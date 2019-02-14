package cn.wenjig.crm.data.entity;

import cn.wenjig.crm.util.JsonUtil;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee_job")
public class EoJ implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private long id;

    @Column(name = "employee_id")
    private long employeeId;

    @Column(name = "job_id")
    private long jobId;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof EoJ) {
            return this.getId() == ((EoJ) obj).getId();
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }
}
