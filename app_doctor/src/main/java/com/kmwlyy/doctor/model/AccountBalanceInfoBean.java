package com.kmwlyy.doctor.model;

/**
 * @Description描述: 账户余额信息
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/24
 */
public class AccountBalanceInfoBean {
    public String AccountID;
    public int Currency;//货币种类
    public double Balance = 0;//余额
    public double Available = 0;//可用余额
    public double Freeze = 0;//冻结
    public int Status;
}
