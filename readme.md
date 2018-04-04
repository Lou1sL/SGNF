# SGNF Game Network Framework


基于Netty的高效，易用网游C/S架构解决框架

让开发者只需关注业务逻辑本身


## Server端:

### 资料服务器(InfoServer,IS)

处理玩家菜单内行为，如：登录，修改信息，匹配战局

### 场景服务器(ScenarioServer,SS)

同步游戏场景

### 负载均衡的实现：

* 采用将IS与SS分离，SS与SS分离的方法

* IS在玩家匹配战局时通过玩家Ping及SS负载选取最优SS

* 在单服务器下，可将IS和SS绑定在本机不同端口上使用

## Client:

### 暂时只做Unity端








> She's GoNe Forever