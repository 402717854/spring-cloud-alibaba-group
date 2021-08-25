package com.springcloud.alibaba.demo.seata.account.domain.account.entity;

/**
 * <p>
 * 
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public class TAccount {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String userId;
    private Double amount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TAccount{" +
        ", id=" + id +
        ", userId=" + userId +
        ", amount=" + amount +
        "}";
    }
}
