package com.springcloud.alibaba.demo.seata.common.response;

import java.io.Serializable;

/**
 * @author: wys
 * @date: 2021/08/18 16:46
 */
public class ObjectResponse<T> extends BaseResponse implements Serializable {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
