# Parking Lot
###这是一个训练项目，完成停车场相关功能：  
###数据库部分：
数据表设计  
 *  staff（id、name、role[外键]、create_date、remove_date）  
 *  role（id、role）  
 *  basement（id、size）  
 *  ticket（id、entry_date、parking_lot_id[外键]、storage_id[外键])
 *  car (id)
 *  storage (id、address、car_id[外键]、parking_id[外键])

###后台部分：  
完成功能：
1.客户自己停车 2.检查小票和所取车辆后取车 3.聪明和普通停车小弟按各自逻辑帮助停车 4.经理帮助停车 5.经理管理停车场和停车小弟

How to start this project:
1.set up mysql database
>docker-compose -f docker-mysql-local.yml up

2.run flyway migrate to migrate data
> gradle flywaymigrate

3.run LotApplication

then you can visit this project in localhost:8080


-------------------
本次做的修改：
1.拆分了controller
2.拆分了service
3.去掉了没必要的属性
4.给parking增加了emptyNumber字段
5.修改了校验ticket方式
6.整理test
7.更新readme