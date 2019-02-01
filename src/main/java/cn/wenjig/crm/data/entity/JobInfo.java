package cn.wenjig.crm.data.entity;

import cn.wenjig.crm.util.JsonUtil;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "job")
public class JobInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private long id;

    @Column(name = "job", nullable = false, length = 45)
    private String job;

    @Column(name = "department_id")
    private long departmentId;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof JobInfo) {
            return this.getId() == ((JobInfo) obj).getId();
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }
}
