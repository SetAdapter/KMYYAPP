package com.kmwlyy.patient.module.InhabitantStart;

/**
 * Created by Administrator on 2017/8/7.
 */

public class Memberbean {
    private String name;
    private String relation;

    public Memberbean(String name, String relation) {
        this.name = name;
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
