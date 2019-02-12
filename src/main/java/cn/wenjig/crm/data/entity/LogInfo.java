package cn.wenjig.crm.data.entity;

import cn.wenjig.crm.util.JsonUtil;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "log_info")
public class LogInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private long id;

    @Column(name = "employee_id")
    private long employeeId;

    @Column(name = "method", nullable = false, length = 255)
    private String method;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "run_time")
    private long runTime;

    @Column(name = "args", columnDefinition = "TEXT")
    private String args;

    @Column(name = "operating_type", length = 100)
    private String operating_type;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "return_value", length = 255)
    private String returnValue;

    @Column(name = "level")
    private int level;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getOperating_type() {
        return operating_type;
    }

    public void setOperating_type(String operating_type) {
        this.operating_type = operating_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
