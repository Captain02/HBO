package io.renren.modules.activity.Entity;

import org.springframework.stereotype.Component;

public class ActState {

    private Long proid;

    private Integer type;

    public Long getProid() {
        return proid;
    }

    public void setProid(Long proid) {
        this.proid = proid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ActState() {
    }


}
