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


需求分解：
1.自己选择停车场停车，获取ticket
2.聪明停车小弟帮助选取空位数最多的停车场停车
3.普通停车小弟帮助按顺序选择停车场停车
4.经理帮助停车每次算法都是随机按聪明的或者不聪明的方法来选取停车场，不管是不是同一个经理
5.提供要取的车辆id和ticket id取车，
取车时要检验停车场中是否有这个车位以及该车位上是否停的是该牌照的车
6.增加停车场，增加停车场的同时按顺序生成好storage
7.删除停车场，如果该停车场的车位上还停有车，则删除失败，抛出异常
8.增加员工
9.注销员工