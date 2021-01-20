# Parking Lot
###这是一个训练项目，完成停车场相关功能，项目tasking如下：  
###数据库部分：  
数据库选用  
数据表设计  
 *  人员表（id、name、role[外键]、create_date、remove_date）  
 *  角色表（id、role）  
 *  停车场表（id、size）  
 *  小票表（id、timeout_date、parking_lot_id[外键])  
 
创建数据表
###后台部分：  
框架搭建（springboot+gradle+junit）  
停车场停取车功能  
  * 编写测试  
    *   停车获得小票  
    *   用小票取车  
        * 用合法小票可以取车  
        * 用不合法小票不能取车  
           *   正规开具但是超过时效的小票不能取车  
           *   非正规开具小票不能取车  
    *   停车场不能停过多的车  
  * 编写停车场类  
  * 实现停放超过停车场上限的车抛出异常的方法  
  * 实现停取车功能  
    *   实现停车方法  
        * 编写小票类  
        * 实现开具小票方法  
    *   实现通过小票取车方法  
        *   实现小票验证方法  
        
通过停车小弟寻找停车场功能  
  * 编写测试  
    *   可以获取全部停车场  
  * 实现获取全部停车场功能  
    *   实现验证身份功能  
  * 普通停车小弟寻找停车场功能  
    *   编写测试  
        * 可以按顺序将车停到停车场  
           *    如果下一个停车场有空位，正常停入  
           *    如果下一个停车场没有空位，寻找之后的有空位的停车场停入   
    *   实现按顺序停车的方法  
  * 聪明小弟寻找停车场功能  
     *  编写测试  
        *   优先将车停到空位多的停车场  
            *    最多空位有多个停车场按顺序来选  
            *    最多空位只有一个则选择这个停车  
     *  实现优先停到空位最多的停车场  
        *   实现获得空位最多停车场集合的方法  
        
通过经理停车的功能  
  * 编写测试  
    *   经理可以按用户需求决定是按顺序停车还是选空位最多的停车场停车  
        *    如果参数传入true则选空位最多的停车场停车  
        *    如果传入false则按顺序停车  
  * 编写经理类继承基类  
  * 完成经理停车方法  
  
管理停车小弟功能  
  * 编写测试  
    *   添加停车小弟  
    *   开除停车小弟  
  * 实现添加停车小弟方法  
  * 实现开除停车小弟方法  
  
管理停车场功能  
  * 编写测试  
    *   添加停车场  
    *   删除停车场  
  * 实现添加停车场方法  
  * 实现删除停车场方法  