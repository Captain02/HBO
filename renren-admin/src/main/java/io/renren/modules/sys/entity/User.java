package io.renren.modules.sys.entity;

public class User {
    private String name;
    private String mobile;
    private String status;
    private String persionnum;
    private String wechart;
    private String QQ;
    private String college;
    private String collegemajor;
    private String gender;

    public User() {
    }

    public User(String name, String mobile, String status, String persionnum, String wechart, String QQ, String college, String collegemajor, String gender) {
        this.name = name;
        this.mobile = mobile;
        this.status = status;
        this.persionnum = persionnum;
        this.wechart = wechart;
        this.QQ = QQ;
        this.college = college;
        this.collegemajor = collegemajor;
        this.gender = gender;
    }

    public User(String name, String mobile, String persionnum, String wechart, String QQ, String college, String collegemajor, String gender) {
        this.name = name;
        this.mobile = mobile;
        this.persionnum = persionnum;
        this.wechart = wechart;
        this.QQ = QQ;
        this.college = college;
        this.collegemajor = collegemajor;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPersionnum() {
        return persionnum;
    }

    public void setPersionnum(String persionnum) {
        this.persionnum = persionnum;
    }

    public String getWechart() {
        return wechart;
    }

    public void setWechart(String wechart) {
        this.wechart = wechart;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCollegemajor() {
        return collegemajor;
    }

    public void setCollegemajor(String collegemajor) {
        this.collegemajor = collegemajor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", persionnum='" + persionnum + '\'' +
                ", wechart='" + wechart + '\'' +
                ", QQ='" + QQ + '\'' +
                ", college='" + college + '\'' +
                ", collegemajor='" + collegemajor + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
