###官方文档
    http://seata.io/zh-cn/index.html

TCC模式
配置文件:
    #====================================Seata Config===============================================
    
    seata:
        enabled: true
        application-id: account-seata-example
        tx-service-group: account-seata-service-group # 事务群组（可以每个应用独立取名，也可以使用相同的名字）
        service:
            vgroup-mapping:
                account-seata-service-group: default # TC 集群（必须与seata-server保持一致）
            enable-degrade: false # 降级开关
                disable-global-transaction: false # 禁用全局事务（默认false）
            grouplist:
                default: 192.168.66.9:8091

本地TCC bean需要在 接口定义中添加 @LocalTCC 注解，其定义如下：

- AccountAction 接口定义如下：
  
      @LocalTCC
      public interface AccountAction {
      /**
        * 扣用户钱
          */
          @TwoPhaseBusinessAction(name = "account-decrease",commitMethod = "commit",rollbackMethod = "rollback")
          boolean decreaseAccount(@BusinessActionContextParameter(paramName = "userId")String userId,@BusinessActionContextParameter(paramName = "amount") BigDecimal amount);
          boolean commit(BusinessActionContext businessActionContext);
          boolean rollback(BusinessActionContext businessActionContext);
          }