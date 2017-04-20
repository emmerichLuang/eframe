反正，还是用module的方式， 把常用的东西丢到这里更好。

目前整理有：

- core：spring辅助；
	- @Transaction
	- @RowCache 必须要一个key
	- @MasterDatasource/@SlaveDatasource
	
- dataAccess：数据访问；
	- 基本的增删改查
	- 缓存	
	- 多数据源
		- 读写分离
		- 分库分表

- rpcAccess：访问外部接口；
	- 拒绝服务与恢复
	
- spider：抓数据，爬虫~
- workflow：工作流
- priority：权限。

