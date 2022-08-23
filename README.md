# 分布式开发学习

## 分布式基础概念

### 微服务

把一整个应用根据业务不同拆分为不同的小服务，每个小服务运行在自己的进程当中（根据进程线程概念可知，此时每个小服务都是单独的应用了），之间进行轻量级通信，通常是http API。并且通过自动化部署机制来独立部署小服务。这些服务之间属于进程间通信，因此可以采用不同的编程语言书写，或者是不同的数据库进行存储，并保持最低限度的集中式管理。

### 集群&分布式&节点

集群是一种物理上的形态；分布式是一种工作方式。

一堆机器，叫做集群，是不是一起协作干活，这个视情况而定，通常情况下是协作的。

分布式系统是若干独立计算机的集合，而分布式系统展示给用户的就像是一个完整的系统。分布式系统是建立在网络之上的软件系统。

分布式系统将不同的服务分布在不同的机器上，然后一个服务可能要多台机器共同实现，这就是集群。所有业务构成业务集群，也可以理解为一个业务集群对应一个完整的应用。

### 远程调用

分布式系统中，不同服务的相互调用，就需要远程调用，SpringCloud建议使用http+json完成远程调用。

### 负载均衡

一个服务部署在集群中，那么我们肯定希望让集群中的机器轮番处理服务，而不是让一台全天候工作，其余几台完全不干活。这就需要负载均衡算法进行干预。常见的负载均衡算法：轮询，最小连接、散列

1. **轮询**，每次服务都调用池子中最后一台机器，调用过的机器往前提。
2. **最小连接**，对于一个新的请求，与当前池子中已有连接数量最小的机器建立连接，在会话较长的时候，使用这种。
3. **散列**，根据用户的ip地址的散列（hash)，来使得用户可以一直连接到固定机器。



### 服务注册/发现&注册中心

服务A调用服务B的时候，并不知道服务B部署在哪里，以及服务B的集群中哪些机器是可以使用的。这时候需要注册中心（可以理解为转接台）。当服务B上线的时候，需要告知注册中心，“服务B已上线，可以机器为1，2，3。”

当服务B下线的时候，也需要告知注册中心，“服务B已经下线，机器1，2，3不可用。”

### 配置中心

每一个服务都需要大量的配置，并且服务可能部署在多台机器，那么为了实现自动化部署，需要将服务所需要的配置上传到配置中心。部署服务的时候只需要去配置中心查找当前服务所需要的配置就好。

当配置更改的时候，只需要更改配置中心的内容，已经部署的服务会自动更新到最新配置。

*配置中心用来集中管理服务的配置信息。*

### 服务熔断&服务降级

微服务架构中，微服务之间相互通信，具有依赖性，当一个服务不可用，可能会导致雪崩。为了避免这种情况，需要有容错机制来保护服务。

下面是一个假设情况，A-->B-->C-->D，由于D服务宕机，或者需要大量时间去处理，那么就会影响C服务，进而影响B、A服务。当有大量A服务请求的时候，就会出现严重的情况。请求全部阻塞，服务器资源耗尽。

1. 服务熔断

​	C服务请求D服务的时候，假如3s内还没有得到响应，那么就是请求失败。设定阈值，假如每秒100次请求全部失败。触发熔断机制。此时如果C服务需要调用D服务的时候，将不再调用，而是直接将本该D服务返回的值设置为一个默认值向后传递。C-->D的请求链直接断裂。

​	那么A-->B-->C这段服务不会阻塞。

2. 服务降级

   让非核心业务降级运行，降级：不处理、简单处理（返回NULL，调用Mock函数、抛异常等）。

比如双十一订单结算的时候，往往会直接返回创建订单失败的结果，这背后就有服务熔断/降级机制。

### API网关

在微服务架构中，API Gateway是整体架构的重要组件，它*抽象了微服务中都需要的公共功能*，同时提供了客户端**负载均衡、服务自动熔断、灰度发布、统一认证、限流流控、日志统计**等功能。

![image-20220722145708442](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220722145708442.png)



API网关组件：Spring Cloud Gateway

熔断组件：Sentinel

开发微服务：Spring Boot

微服务通信组件：Feign

安全控制：Spring Security

缓存：Redis

持久化：MySQL（分库分表、读写分离）

服务与服务之间使用消息队列（MQ）进行消息解耦。完成分布式服务的最终一致性。Rabbit MQ。

有些服务需要检索信息，全文检索组件：ElasticSearch。

注册中心：Nacos

配置中心：Nacos

![image-20220722151050473](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220722151050473.png)

## Linux安装

使用Virtual BOX + Vagrant安装ctenos7。

由于网络问题，可以先在vagrant的[官网](https://app.vagrantup.com/centos/boxes/7/versions/2004.01)上下载ctenos的镜像，将镜像文件放入到本地vagrant的文件夹中（和lib文件夹同级即可）。

```shell
vagrant box add ctenos7 ./CentOS-7-x86_64-Vagrant-2004_01.VirtualBox.box
```

将下载的镜像添加到vagrant的box中，然后执行

```shell
vagrant init ctenos7
```

会在`C:\Users\用户名`中生成一个`Vagrantfile`的文件，然后执行

```shell
vagrant up
```

等待安装成功，当出现下图的时候，就代表安装成功。

![image-20220722222314528](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220722222314528.png)

然后执行命令`vagrant ssh`，就可以连接到虚拟机。

>  *SSH* (Secure Shell) 是一种网络协议，可实现两个设备之间的安全通信。

在`Vagrantfile`文件中可以配置虚拟机的`IP`地址，而不是通过端口映射访问。

![image-20220723172547550](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220723172547550.png)

然后执行命令`vagrant reload`重启虚拟机即可。

## Docker安装

Docker官网已经没有显示ctenos的安装，这里找了一个[参考安装教程](https://blog.csdn.net/PyongSen/article/details/123053374)。

首先卸载旧版本的docker

```shell
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

然后安装包

```shell
sudo yum install -y yum-utils
```

然后设置镜像仓库，这里使用阿里云的镜像仓库

```shell
sudo yum-config-manager \
    --add-repo \
    http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

安装前先更新yum软件包索引

```shell
yum makecache fast
```

然后安装社区版docker

```shell
sudo yum install docker-ce docker-ce-cli containerd.io
```

启动docker

```shell
sudo systemctl start docker
```

判断是否成功安装docker 查看版本

```shell
docker -v
```

设置docker开机自启动

```shell
sudo systemctl enable docker
```

设置docker的镜像加速器

```shell
sudo mkdir -p /etc/docker
```

```shell
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://xuxqs7wq.mirror.aliyuncs.com"]
}
EOF
```

```shell
sudo systemctl daemon-reload
```

```shell
sudo systemctl restart docker
```

## MySQL、Redis安装

执行命令

```shell
sudo docker pull mysql:5.7
```

第一次安装mysql遇到pull的时候一直waiting的问题，重新安装docker得到解决。

启动mysql容器

> --name指定容器名字 -v目录挂载 -p指定端口映射  -e设置mysql参数 -d后台运行

```shell
sudo docker run --name mysql -v /usr/local/mysql/data:/var/lib/mysql -v /usr/local/mysql:/etc/mysql/conf.d -v /usr/local/mysql/log:/var/log/mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:5.7
```

修改配置文件

```shell
vi /mydata/mysal/conf/iny.cnf

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
skip-name-resolve

```



#### Redis




```shell
docker pull redis
```

 创建目录结构

```shell
mkdir -p /mydata/redis/conf
```

创建配置文件

```shell
touch /mydata/redis/conf/redis.conf
```

安装redis（并挂载配置文件）

```shell
docker run -p 6379:6379 --name redis -v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf
```

 连接到docker的redis控制台

```shell
docker exec -it redis redis-cli
```

测试redis（exit：退出）

```shell
127.0.0.1:6379> set a b
OK
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> exit
```

重启redis

```shell
docker restart redis
```

修改redis配置文件（设置持久化）

```shell
appendonly yes
```

新版本的redis已经默认AOF持久化。



## 数据库创建

**OMS**

```mysql
gulimall-oms.sql

drop table if exists oms_order;

drop table if exists oms_order_item;

drop table if exists oms_order_operate_history;

drop table if exists oms_order_return_apply;

drop table if exists oms_order_return_reason;

drop table if exists oms_order_setting;

drop table if exists oms_payment_info;

drop table if exists oms_refund_info;

/*==============================================================*/
/* Table: oms_order                                             */
/*==============================================================*/
create table oms_order
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   order_sn             char(32) comment '订单号',
   coupon_id            bigint comment '使用的优惠券',
   create_time          datetime comment 'create_time',
   member_username      varchar(200) comment '用户名',
   total_amount         decimal(18,4) comment '订单总额',
   pay_amount           decimal(18,4) comment '应付总额',
   freight_amount       decimal(18,4) comment '运费金额',
   promotion_amount     decimal(18,4) comment '促销优化金额（促销价、满减、阶梯价）',
   integration_amount   decimal(18,4) comment '积分抵扣金额',
   coupon_amount        decimal(18,4) comment '优惠券抵扣金额',
   discount_amount      decimal(18,4) comment '后台调整订单使用的折扣金额',
   pay_type             tinyint comment '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】',
   source_type          tinyint comment '订单来源[0->PC订单；1->app订单]',
   status               tinyint comment '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】',
   delivery_company     varchar(64) comment '物流公司(配送方式)',
   delivery_sn          varchar(64) comment '物流单号',
   auto_confirm_day     int comment '自动确认时间（天）',
   integration          int comment '可以获得的积分',
   growth               int comment '可以获得的成长值',
   bill_type            tinyint comment '发票类型[0->不开发票；1->电子发票；2->纸质发票]',
   bill_header          varchar(255) comment '发票抬头',
   bill_content         varchar(255) comment '发票内容',
   bill_receiver_phone  varchar(32) comment '收票人电话',
   bill_receiver_email  varchar(64) comment '收票人邮箱',
   receiver_name        varchar(100) comment '收货人姓名',
   receiver_phone       varchar(32) comment '收货人电话',
   receiver_post_code   varchar(32) comment '收货人邮编',
   receiver_province    varchar(32) comment '省份/直辖市',
   receiver_city        varchar(32) comment '城市',
   receiver_region      varchar(32) comment '区',
   receiver_detail_address varchar(200) comment '详细地址',
   note                 varchar(500) comment '订单备注',
   confirm_status       tinyint comment '确认收货状态[0->未确认；1->已确认]',
   delete_status        tinyint comment '删除状态【0->未删除；1->已删除】',
   use_integration      int comment '下单时使用的积分',
   payment_time         datetime comment '支付时间',
   delivery_time        datetime comment '发货时间',
   receive_time         datetime comment '确认收货时间',
   comment_time         datetime comment '评价时间',
   modify_time          datetime comment '修改时间',
   primary key (id)
);

alter table oms_order comment '订单';

/*==============================================================*/
/* Table: oms_order_item                                        */
/*==============================================================*/
create table oms_order_item
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment 'order_id',
   order_sn             char(32) comment 'order_sn',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(255) comment 'spu_name',
   spu_pic              varchar(500) comment 'spu_pic',
   spu_brand            varchar(200) comment '品牌',
   category_id          bigint comment '商品分类id',
   sku_id               bigint comment '商品sku编号',
   sku_name             varchar(255) comment '商品sku名字',
   sku_pic              varchar(500) comment '商品sku图片',
   sku_price            decimal(18,4) comment '商品sku价格',
   sku_quantity         int comment '商品购买的数量',
   sku_attrs_vals       varchar(500) comment '商品销售属性组合（JSON）',
   promotion_amount     decimal(18,4) comment '商品促销分解金额',
   coupon_amount        decimal(18,4) comment '优惠券优惠分解金额',
   integration_amount   decimal(18,4) comment '积分优惠分解金额',
   real_amount          decimal(18,4) comment '该商品经过优惠后的分解金额',
   gift_integration     int comment '赠送积分',
   gift_growth          int comment '赠送成长值',
   primary key (id)
);

alter table oms_order_item comment '订单项信息';

/*==============================================================*/
/* Table: oms_order_operate_history                             */
/*==============================================================*/
create table oms_order_operate_history
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment '订单id',
   operate_man          varchar(100) comment '操作人[用户；系统；后台管理员]',
   create_time          datetime comment '操作时间',
   order_status         tinyint comment '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】',
   note                 varchar(500) comment '备注',
   primary key (id)
);

alter table oms_order_operate_history comment '订单操作历史记录';

/*==============================================================*/
/* Table: oms_order_return_apply                                */
/*==============================================================*/
create table oms_order_return_apply
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment 'order_id',
   sku_id               bigint comment '退货商品id',
   order_sn             char(32) comment '订单编号',
   create_time          datetime comment '申请时间',
   member_username      varchar(64) comment '会员用户名',
   return_amount        decimal(18,4) comment '退款金额',
   return_name          varchar(100) comment '退货人姓名',
   return_phone         varchar(20) comment '退货人电话',
   status               tinyint(1) comment '申请状态[0->待处理；1->退货中；2->已完成；3->已拒绝]',
   handle_time          datetime comment '处理时间',
   sku_img              varchar(500) comment '商品图片',
   sku_name             varchar(200) comment '商品名称',
   sku_brand            varchar(200) comment '商品品牌',
   sku_attrs_vals       varchar(500) comment '商品销售属性(JSON)',
   sku_count            int comment '退货数量',
   sku_price            decimal(18,4) comment '商品单价',
   sku_real_price       decimal(18,4) comment '商品实际支付单价',
   reason               varchar(200) comment '原因',
   description述         varchar(500) comment '描述',
   desc_pics            varchar(2000) comment '凭证图片，以逗号隔开',
   handle_note          varchar(500) comment '处理备注',
   handle_man           varchar(200) comment '处理人员',
   receive_man          varchar(100) comment '收货人',
   receive_time         datetime comment '收货时间',
   receive_note         varchar(500) comment '收货备注',
   receive_phone        varchar(20) comment '收货电话',
   company_address      varchar(500) comment '公司收货地址',
   primary key (id)
);

alter table oms_order_return_apply comment '订单退货申请';

/*==============================================================*/
/* Table: oms_order_return_reason                               */
/*==============================================================*/
create table oms_order_return_reason
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment '退货原因名',
   sort                 int comment '排序',
   status               tinyint(1) comment '启用状态',
   create_time          datetime comment 'create_time',
   primary key (id)
);

alter table oms_order_return_reason comment '退货原因';

/*==============================================================*/
/* Table: oms_order_setting                                     */
/*==============================================================*/
create table oms_order_setting
(
   id                   bigint not null auto_increment comment 'id',
   flash_order_overtime int comment '秒杀订单超时关闭时间(分)',
   normal_order_overtime int comment '正常订单超时时间(分)',
   confirm_overtime     int comment '发货后自动确认收货时间（天）',
   finish_overtime      int comment '自动完成交易时间，不能申请退货（天）',
   comment_overtime     int comment '订单完成后自动好评时间（天）',
   member_level         tinyint(2) comment '会员等级【0-不限会员等级，全部通用；其他-对应的其他会员等级】',
   primary key (id)
);

alter table oms_order_setting comment '订单配置信息';

/*==============================================================*/
/* Table: oms_payment_info                                      */
/*==============================================================*/
create table oms_payment_info
(
   id                   bigint not null auto_increment comment 'id',
   order_sn             char(32) comment '订单号（对外业务号）',
   order_id             bigint comment '订单id',
   alipay_trade_no      varchar(50) comment '支付宝交易流水号',
   total_amount         decimal(18,4) comment '支付总金额',
   subject              varchar(200) comment '交易内容',
   payment_status       varchar(20) comment '支付状态',
   create_time          datetime comment '创建时间',
   confirm_time         datetime comment '确认时间',
   callback_content     varchar(4000) comment '回调内容',
   callback_time        datetime comment '回调时间',
   primary key (id)
);

alter table oms_payment_info comment '支付信息表';

/*==============================================================*/
/* Table: oms_refund_info                                       */
/*==============================================================*/
create table oms_refund_info
(
   id                   bigint not null auto_increment comment 'id',
   order_return_id      bigint comment '退款的订单',
   refund               decimal(18,4) comment '退款金额',
   refund_sn            varchar(64) comment '退款交易流水号',
   refund_status        tinyint(1) comment '退款状态',
   refund_channel       tinyint comment '退款渠道[1-支付宝，2-微信，3-银联，4-汇款]',
   refund_content       varchar(5000),
   primary key (id)
);

alter table oms_refund_info comment '退款信息';



```

**PMS**

```mysql
gulimall-pms.sql

drop table if exists pms_attr;

drop table if exists pms_attr_attrgroup_relation;

drop table if exists pms_attr_group;

drop table if exists pms_brand;

drop table if exists pms_category;

drop table if exists pms_category_brand_relation;

drop table if exists pms_comment_replay;

drop table if exists pms_product_attr_value;

drop table if exists pms_sku_images;

drop table if exists pms_sku_info;

drop table if exists pms_sku_sale_attr_value;

drop table if exists pms_spu_comment;

drop table if exists pms_spu_images;

drop table if exists pms_spu_info;

drop table if exists pms_spu_info_desc;

/*==============================================================*/
/* Table: pms_attr                                              */
/*==============================================================*/
create table pms_attr
(
   attr_id              bigint not null auto_increment comment '属性id',
   attr_name            char(30) comment '属性名',
   search_type          tinyint comment '是否需要检索[0-不需要，1-需要]',
   icon                 varchar(255) comment '属性图标',
   value_select         char(255) comment '可选值列表[用逗号分隔]',
   attr_type            tinyint comment '属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]',
   enable               bigint comment '启用状态[0 - 禁用，1 - 启用]',
   catelog_id           bigint comment '所属分类',
   show_desc            tinyint comment '快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整',
   primary key (attr_id)
);

alter table pms_attr comment '商品属性';

/*==============================================================*/
/* Table: pms_attr_attrgroup_relation                           */
/*==============================================================*/
create table pms_attr_attrgroup_relation
(
   id                   bigint not null auto_increment comment 'id',
   attr_id              bigint comment '属性id',
   attr_group_id        bigint comment '属性分组id',
   attr_sort            int comment '属性组内排序',
   primary key (id)
);

alter table pms_attr_attrgroup_relation comment '属性&属性分组关联';

/*==============================================================*/
/* Table: pms_attr_group                                        */
/*==============================================================*/
create table pms_attr_group
(
   attr_group_id        bigint not null auto_increment comment '分组id',
   attr_group_name      char(20) comment '组名',
   sort                 int comment '排序',
   descript             varchar(255) comment '描述',
   icon                 varchar(255) comment '组图标',
   catelog_id           bigint comment '所属分类id',
   primary key (attr_group_id)
);

alter table pms_attr_group comment '属性分组';

/*==============================================================*/
/* Table: pms_brand                                             */
/*==============================================================*/
create table pms_brand
(
   brand_id             bigint not null auto_increment comment '品牌id',
   name                 char(50) comment '品牌名',
   logo                 varchar(2000) comment '品牌logo地址',
   descript             longtext comment '介绍',
   show_status          tinyint comment '显示状态[0-不显示；1-显示]',
   first_letter         char(1) comment '检索首字母',
   sort                 int comment '排序',
   primary key (brand_id)
);

alter table pms_brand comment '品牌';

/*==============================================================*/
/* Table: pms_category                                          */
/*==============================================================*/
create table pms_category
(
   cat_id               bigint not null auto_increment comment '分类id',
   name                 char(50) comment '分类名称',
   parent_cid           bigint comment '父分类id',
   cat_level            int comment '层级',
   show_status          tinyint comment '是否显示[0-不显示，1显示]',
   sort                 int comment '排序',
   icon                 char(255) comment '图标地址',
   product_unit         char(50) comment '计量单位',
   product_count        int comment '商品数量',
   primary key (cat_id)
);

alter table pms_category comment '商品三级分类';

/*==============================================================*/
/* Table: pms_category_brand_relation                           */
/*==============================================================*/
create table pms_category_brand_relation
(
   id                   bigint not null auto_increment,
   brand_id             bigint comment '品牌id',
   catelog_id           bigint comment '分类id',
   brand_name           varchar(255),
   catelog_name         varchar(255),
   primary key (id)
);

alter table pms_category_brand_relation comment '品牌分类关联';

/*==============================================================*/
/* Table: pms_comment_replay                                    */
/*==============================================================*/
create table pms_comment_replay
(
   id                   bigint not null auto_increment comment 'id',
   comment_id           bigint comment '评论id',
   reply_id             bigint comment '回复id',
   primary key (id)
);

alter table pms_comment_replay comment '商品评价回复关系';

/*==============================================================*/
/* Table: pms_product_attr_value                                */
/*==============================================================*/
create table pms_product_attr_value
(
   id                   bigint not null auto_increment comment 'id',
   spu_id               bigint comment '商品id',
   attr_id              bigint comment '属性id',
   attr_name            varchar(200) comment '属性名',
   attr_value           varchar(200) comment '属性值',
   attr_sort            int comment '顺序',
   quick_show           tinyint comment '快速展示【是否展示在介绍上；0-否 1-是】',
   primary key (id)
);

alter table pms_product_attr_value comment 'spu属性值';

/*==============================================================*/
/* Table: pms_sku_images                                        */
/*==============================================================*/
create table pms_sku_images
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   img_url              varchar(255) comment '图片地址',
   img_sort             int comment '排序',
   default_img          int comment '默认图[0 - 不是默认图，1 - 是默认图]',
   primary key (id)
);

alter table pms_sku_images comment 'sku图片';

/*==============================================================*/
/* Table: pms_sku_info                                          */
/*==============================================================*/
create table pms_sku_info
(
   sku_id               bigint not null auto_increment comment 'skuId',
   spu_id               bigint comment 'spuId',
   sku_name             varchar(255) comment 'sku名称',
   sku_desc             varchar(2000) comment 'sku介绍描述',
   catalog_id           bigint comment '所属分类id',
   brand_id             bigint comment '品牌id',
   sku_default_img      varchar(255) comment '默认图片',
   sku_title            varchar(255) comment '标题',
   sku_subtitle         varchar(2000) comment '副标题',
   price                decimal(18,4) comment '价格',
   sale_count           bigint comment '销量',
   primary key (sku_id)
);

alter table pms_sku_info comment 'sku信息';

/*==============================================================*/
/* Table: pms_sku_sale_attr_value                               */
/*==============================================================*/
create table pms_sku_sale_attr_value
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   attr_id              bigint comment 'attr_id',
   attr_name            varchar(200) comment '销售属性名',
   attr_value           varchar(200) comment '销售属性值',
   attr_sort            int comment '顺序',
   primary key (id)
);

alter table pms_sku_sale_attr_value comment 'sku销售属性&值';

/*==============================================================*/
/* Table: pms_spu_comment                                       */
/*==============================================================*/
create table pms_spu_comment
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(255) comment '商品名字',
   member_nick_name     varchar(255) comment '会员昵称',
   star                 tinyint(1) comment '星级',
   member_ip            varchar(64) comment '会员ip',
   create_time          datetime comment '创建时间',
   show_status          tinyint(1) comment '显示状态[0-不显示，1-显示]',
   spu_attributes       varchar(255) comment '购买时属性组合',
   likes_count          int comment '点赞数',
   reply_count          int comment '回复数',
   resources            varchar(1000) comment '评论图片/视频[json数据；[{type:文件类型,url:资源路径}]]',
   content              text comment '内容',
   member_icon          varchar(255) comment '用户头像',
   comment_type         tinyint comment '评论类型[0 - 对商品的直接评论，1 - 对评论的回复]',
   primary key (id)
);

alter table pms_spu_comment comment '商品评价';

/*==============================================================*/
/* Table: pms_spu_images                                        */
/*==============================================================*/
create table pms_spu_images
(
   id                   bigint not null auto_increment comment 'id',
   spu_id               bigint comment 'spu_id',
   img_name             varchar(200) comment '图片名',
   img_url              varchar(255) comment '图片地址',
   img_sort             int comment '顺序',
   default_img          tinyint comment '是否默认图',
   primary key (id)
);

alter table pms_spu_images comment 'spu图片';

/*==============================================================*/
/* Table: pms_spu_info                                          */
/*==============================================================*/
create table pms_spu_info
(
   id                   bigint not null auto_increment comment '商品id',
   spu_name             varchar(200) comment '商品名称',
   spu_description      varchar(1000) comment '商品描述',
   catalog_id           bigint comment '所属分类id',
   brand_id             bigint comment '品牌id',
   weight               decimal(18,4),
   publish_status       tinyint comment '上架状态[0 - 下架，1 - 上架]',
   create_time          datetime,
   update_time          datetime,
   primary key (id)
);

alter table pms_spu_info comment 'spu信息';

/*==============================================================*/
/* Table: pms_spu_info_desc                                     */
/*==============================================================*/
create table pms_spu_info_desc
(
   spu_id               bigint not null comment '商品id',
   decript              longtext comment '商品介绍',
   primary key (spu_id)
);

alter table pms_spu_info_desc comment 'spu信息介绍';



```



**SMS**

```mysql

drop table if exists sms_coupon;

drop table if exists sms_coupon_history;

drop table if exists sms_coupon_spu_category_relation;

drop table if exists sms_coupon_spu_relation;

drop table if exists sms_home_adv;

drop table if exists sms_home_subject;

drop table if exists sms_home_subject_spu;

drop table if exists sms_member_price;

drop table if exists sms_seckill_promotion;

drop table if exists sms_seckill_session;

drop table if exists sms_seckill_sku_notice;

drop table if exists sms_seckill_sku_relation;

drop table if exists sms_sku_full_reduction;

drop table if exists sms_sku_ladder;

drop table if exists sms_spu_bounds;

/*==============================================================*/
/* Table: sms_coupon                                            */
/*==============================================================*/
create table sms_coupon
(
   id                   bigint not null auto_increment comment 'id',
   coupon_type          tinyint(1) comment '优惠卷类型[0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券]',
   coupon_img           varchar(2000) comment '优惠券图片',
   coupon_name          varchar(100) comment '优惠卷名字',
   num                  int comment '数量',
   amount               decimal(18,4) comment '金额',
   per_limit            int comment '每人限领张数',
   min_point            decimal(18,4) comment '使用门槛',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   use_type             tinyint(1) comment '使用类型[0->全场通用；1->指定分类；2->指定商品]',
   note                 varchar(200) comment '备注',
   publish_count        int(11) comment '发行数量',
   use_count            int(11) comment '已使用数量',
   receive_count        int(11) comment '领取数量',
   enable_start_time    datetime comment '可以领取的开始日期',
   enable_end_time      datetime comment '可以领取的结束日期',
   code                 varchar(64) comment '优惠码',
   member_level         tinyint(1) comment '可以领取的会员等级[0->不限等级，其他-对应等级]',
   publish              tinyint(1) comment '发布状态[0-未发布，1-已发布]',
   primary key (id)
);

alter table sms_coupon comment '优惠券信息';

/*==============================================================*/
/* Table: sms_coupon_history                                    */
/*==============================================================*/
create table sms_coupon_history
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment '优惠券id',
   member_id            bigint comment '会员id',
   member_nick_name     varchar(64) comment '会员名字',
   get_type             tinyint(1) comment '获取方式[0->后台赠送；1->主动领取]',
   create_time          datetime comment '创建时间',
   use_type             tinyint(1) comment '使用状态[0->未使用；1->已使用；2->已过期]',
   use_time             datetime comment '使用时间',
   order_id             bigint comment '订单id',
   order_sn             bigint comment '订单号',
   primary key (id)
);

alter table sms_coupon_history comment '优惠券领取历史记录';

/*==============================================================*/
/* Table: sms_coupon_spu_category_relation                      */
/*==============================================================*/
create table sms_coupon_spu_category_relation
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment '优惠券id',
   category_id          bigint comment '产品分类id',
   category_name        varchar(64) comment '产品分类名称',
   primary key (id)
);

alter table sms_coupon_spu_category_relation comment '优惠券分类关联';

/*==============================================================*/
/* Table: sms_coupon_spu_relation                               */
/*==============================================================*/
create table sms_coupon_spu_relation
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment '优惠券id',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(255) comment 'spu_name',
   primary key (id)
);

alter table sms_coupon_spu_relation comment '优惠券与产品关联';

/*==============================================================*/
/* Table: sms_home_adv                                          */
/*==============================================================*/
create table sms_home_adv
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(100) comment '名字',
   pic                  varchar(500) comment '图片地址',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   status               tinyint(1) comment '状态',
   click_count          int comment '点击数',
   url                  varchar(500) comment '广告详情连接地址',
   note                 varchar(500) comment '备注',
   sort                 int comment '排序',
   publisher_id         bigint comment '发布者',
   auth_id              bigint comment '审核者',
   primary key (id)
);

alter table sms_home_adv comment '首页轮播广告';

/*==============================================================*/
/* Table: sms_home_subject                                      */
/*==============================================================*/
create table sms_home_subject
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment '专题名字',
   title                varchar(255) comment '专题标题',
   sub_title            varchar(255) comment '专题副标题',
   status               tinyint(1) comment '显示状态',
   url                  varchar(500) comment '详情连接',
   sort                 int comment '排序',
   img                  varchar(500) comment '专题图片地址',
   primary key (id)
);

alter table sms_home_subject comment '首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】';

/*==============================================================*/
/* Table: sms_home_subject_spu                                  */
/*==============================================================*/
create table sms_home_subject_spu
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment '专题名字',
   subject_id           bigint comment '专题id',
   spu_id               bigint comment 'spu_id',
   sort                 int comment '排序',
   primary key (id)
);

alter table sms_home_subject_spu comment '专题商品';

/*==============================================================*/
/* Table: sms_member_price                                      */
/*==============================================================*/
create table sms_member_price
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   member_level_id      bigint comment '会员等级id',
   member_level_name    varchar(100) comment '会员等级名',
   member_price         decimal(18,4) comment '会员对应价格',
   add_other            tinyint(1) comment '可否叠加其他优惠[0-不可叠加优惠，1-可叠加]',
   primary key (id)
);

alter table sms_member_price comment '商品会员价格';

/*==============================================================*/
/* Table: sms_seckill_promotion                                 */
/*==============================================================*/
create table sms_seckill_promotion
(
   id                   bigint not null auto_increment comment 'id',
   title                varchar(255) comment '活动标题',
   start_time           datetime comment '开始日期',
   end_time             datetime comment '结束日期',
   status               tinyint comment '上下线状态',
   create_time          datetime comment '创建时间',
   user_id              bigint comment '创建人',
   primary key (id)
);

alter table sms_seckill_promotion comment '秒杀活动';

/*==============================================================*/
/* Table: sms_seckill_session                                   */
/*==============================================================*/
create table sms_seckill_session
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment '场次名称',
   start_time           datetime comment '每日开始时间',
   end_time             datetime comment '每日结束时间',
   status               tinyint(1) comment '启用状态',
   create_time          datetime comment '创建时间',
   primary key (id)
);

alter table sms_seckill_session comment '秒杀活动场次';

/*==============================================================*/
/* Table: sms_seckill_sku_notice                                */
/*==============================================================*/
create table sms_seckill_sku_notice
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   sku_id               bigint comment 'sku_id',
   session_id           bigint comment '活动场次id',
   subcribe_time        datetime comment '订阅时间',
   send_time            datetime comment '发送时间',
   notice_type          tinyint(1) comment '通知方式[0-短信，1-邮件]',
   primary key (id)
);

alter table sms_seckill_sku_notice comment '秒杀商品通知订阅';

/*==============================================================*/
/* Table: sms_seckill_sku_relation                              */
/*==============================================================*/
create table sms_seckill_sku_relation
(
   id                   bigint not null auto_increment comment 'id',
   promotion_id         bigint comment '活动id',
   promotion_session_id bigint comment '活动场次id',
   sku_id               bigint comment '商品id',
   seckill_price        decimal comment '秒杀价格',
   seckill_count        decimal comment '秒杀总量',
   seckill_limit        decimal comment '每人限购数量',
   seckill_sort         int comment '排序',
   primary key (id)
);

alter table sms_seckill_sku_relation comment '秒杀活动商品关联';

/*==============================================================*/
/* Table: sms_sku_full_reduction                                */
/*==============================================================*/
create table sms_sku_full_reduction
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'spu_id',
   full_price           decimal(18,4) comment '满多少',
   reduce_price         decimal(18,4) comment '减多少',
   add_other            tinyint(1) comment '是否参与其他优惠',
   primary key (id)
);

alter table sms_sku_full_reduction comment '商品满减信息';

/*==============================================================*/
/* Table: sms_sku_ladder                                        */
/*==============================================================*/
create table sms_sku_ladder
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'spu_id',
   full_count           int comment '满几件',
   discount             decimal(4,2) comment '打几折',
   price                decimal(18,4) comment '折后价',
   add_other            tinyint(1) comment '是否叠加其他优惠[0-不可叠加，1-可叠加]',
   primary key (id)
);

alter table sms_sku_ladder comment '商品阶梯价格';

/*==============================================================*/
/* Table: sms_spu_bounds                                        */
/*==============================================================*/
create table sms_spu_bounds
(
   id                   bigint not null auto_increment comment 'id',
   spu_id               bigint,
   grow_bounds          decimal(18,4) comment '成长积分',
   buy_bounds           decimal(18,4) comment '购物积分',
   work                 tinyint(1) comment '优惠生效情况[1111（四个状态位，从右到左）;0 - 无优惠，成长积分是否赠送;1 - 无优惠，购物积分是否赠送;2 - 有优惠，成长积分是否赠送;3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]',
   primary key (id)
);

alter table sms_spu_bounds comment '商品spu积分设置';



```

**UMS**

```mysql
gulimall_ums.sql

drop table if exists ums_growth_change_history;

drop table if exists ums_integration_change_history;

drop table if exists ums_member;

drop table if exists ums_member_collect_spu;

drop table if exists ums_member_collect_subject;

drop table if exists ums_member_level;

drop table if exists ums_member_login_log;

drop table if exists ums_member_receive_address;

drop table if exists ums_member_statistics_info;

/*==============================================================*/
/* Table: ums_growth_change_history                             */
/*==============================================================*/
create table ums_growth_change_history
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   create_time          datetime comment 'create_time',
   change_count         int comment '改变的值（正负计数）',
   note                 varchar(0) comment '备注',
   source_type          tinyint comment '积分来源[0-购物，1-管理员修改]',
   primary key (id)
);

alter table ums_growth_change_history comment '成长值变化历史记录';

/*==============================================================*/
/* Table: ums_integration_change_history                        */
/*==============================================================*/
create table ums_integration_change_history
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   create_time          datetime comment 'create_time',
   change_count         int comment '变化的值',
   note                 varchar(255) comment '备注',
   source_tyoe          tinyint comment '来源[0->购物；1->管理员修改;2->活动]',
   primary key (id)
);

alter table ums_integration_change_history comment '积分变化历史记录';

/*==============================================================*/
/* Table: ums_member                                            */
/*==============================================================*/
create table ums_member
(
   id                   bigint not null auto_increment comment 'id',
   level_id             bigint comment '会员等级id',
   username             char(64) comment '用户名',
   password             varchar(64) comment '密码',
   nickname             varchar(64) comment '昵称',
   mobile               varchar(20) comment '手机号码',
   email                varchar(64) comment '邮箱',
   header               varchar(500) comment '头像',
   gender               tinyint comment '性别',
   birth                date comment '生日',
   city                 varchar(500) comment '所在城市',
   job                  varchar(255) comment '职业',
   sign                 varchar(255) comment '个性签名',
   source_type          tinyint comment '用户来源',
   integration          int comment '积分',
   growth               int comment '成长值',
   status               tinyint comment '启用状态',
   create_time          datetime comment '注册时间',
   primary key (id)
);

alter table ums_member comment '会员';

/*==============================================================*/
/* Table: ums_member_collect_spu                                */
/*==============================================================*/
create table ums_member_collect_spu
(
   id                   bigint not null comment 'id',
   member_id            bigint comment '会员id',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(500) comment 'spu_name',
   spu_img              varchar(500) comment 'spu_img',
   create_time          datetime comment 'create_time',
   primary key (id)
);

alter table ums_member_collect_spu comment '会员收藏的商品';

/*==============================================================*/
/* Table: ums_member_collect_subject                            */
/*==============================================================*/
create table ums_member_collect_subject
(
   id                   bigint not null auto_increment comment 'id',
   subject_id           bigint comment 'subject_id',
   subject_name         varchar(255) comment 'subject_name',
   subject_img          varchar(500) comment 'subject_img',
   subject_urll         varchar(500) comment '活动url',
   primary key (id)
);

alter table ums_member_collect_subject comment '会员收藏的专题活动';

/*==============================================================*/
/* Table: ums_member_level                                      */
/*==============================================================*/
create table ums_member_level
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(100) comment '等级名称',
   growth_point         int comment '等级需要的成长值',
   default_status       tinyint comment '是否为默认等级[0->不是；1->是]',
   free_freight_point   decimal(18,4) comment '免运费标准',
   comment_growth_point int comment '每次评价获取的成长值',
   priviledge_free_freight tinyint comment '是否有免邮特权',
   priviledge_member_price tinyint comment '是否有会员价格特权',
   priviledge_birthday  tinyint comment '是否有生日特权',
   note                 varchar(255) comment '备注',
   primary key (id)
);

alter table ums_member_level comment '会员等级';

/*==============================================================*/
/* Table: ums_member_login_log                                  */
/*==============================================================*/
create table ums_member_login_log
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   create_time          datetime comment '创建时间',
   ip                   varchar(64) comment 'ip',
   city                 varchar(64) comment 'city',
   login_type           tinyint(1) comment '登录类型[1-web，2-app]',
   primary key (id)
);

alter table ums_member_login_log comment '会员登录记录';

/*==============================================================*/
/* Table: ums_member_receive_address                            */
/*==============================================================*/
create table ums_member_receive_address
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   name                 varchar(255) comment '收货人姓名',
   phone                varchar(64) comment '电话',
   post_code            varchar(64) comment '邮政编码',
   province             varchar(100) comment '省份/直辖市',
   city                 varchar(100) comment '城市',
   region               varchar(100) comment '区',
   detail_address       varchar(255) comment '详细地址(街道)',
   areacode             varchar(15) comment '省市区代码',
   default_status       tinyint(1) comment '是否默认',
   primary key (id)
);

alter table ums_member_receive_address comment '会员收货地址';

/*==============================================================*/
/* Table: ums_member_statistics_info                            */
/*==============================================================*/
create table ums_member_statistics_info
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment '会员id',
   consume_amount       decimal(18,4) comment '累计消费金额',
   coupon_amount        decimal(18,4) comment '累计优惠金额',
   order_count          int comment '订单数量',
   coupon_count         int comment '优惠券数量',
   comment_count        int comment '评价数',
   return_order_count   int comment '退货数量',
   login_count          int comment '登录次数',
   attend_count         int comment '关注数量',
   fans_count           int comment '粉丝数量',
   collect_product_count int comment '收藏的商品数量',
   collect_subject_count int comment '收藏的专题活动数量',
   collect_comment_count int comment '收藏的评论数量',
   invite_friend_count  int comment '邀请的朋友数量',
   primary key (id)
);

alter table ums_member_statistics_info comment '会员统计信息';



```



**WMS**

```mysql
gulimall_wms.sql

/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.27 : Database - gulimall_wms
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gulimall_wms` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `gulimall_wms`;

/*Table structure for table `undo_log` */

DROP TABLE IF EXISTS `undo_log`;

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `undo_log` */

/*Table structure for table `wms_purchase` */

DROP TABLE IF EXISTS `wms_purchase`;

CREATE TABLE `wms_purchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assignee_id` bigint(20) DEFAULT NULL,
  `assignee_name` varchar(255) DEFAULT NULL,
  `phone` char(13) DEFAULT NULL,
  `priority` int(4) DEFAULT NULL,
  `status` int(4) DEFAULT NULL,
  `ware_id` bigint(20) DEFAULT NULL,
  `amount` decimal(18,4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购信息';

/*Data for the table `wms_purchase` */

/*Table structure for table `wms_purchase_detail` */

DROP TABLE IF EXISTS `wms_purchase_detail`;

CREATE TABLE `wms_purchase_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_id` bigint(20) DEFAULT NULL COMMENT '采购单id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT '采购商品id',
  `sku_num` int(11) DEFAULT NULL COMMENT '采购数量',
  `sku_price` decimal(18,4) DEFAULT NULL COMMENT '采购金额',
  `ware_id` bigint(20) DEFAULT NULL COMMENT '仓库id',
  `status` int(11) DEFAULT NULL COMMENT '状态[0新建，1已分配，2正在采购，3已完成，4采购失败]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `wms_purchase_detail` */

/*Table structure for table `wms_ware_info` */

DROP TABLE IF EXISTS `wms_ware_info`;

CREATE TABLE `wms_ware_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) DEFAULT NULL COMMENT '仓库名',
  `address` varchar(255) DEFAULT NULL COMMENT '仓库地址',
  `areacode` varchar(20) DEFAULT NULL COMMENT '区域编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库信息';

/*Data for the table `wms_ware_info` */

/*Table structure for table `wms_ware_order_task` */

DROP TABLE IF EXISTS `wms_ware_order_task`;

CREATE TABLE `wms_ware_order_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) DEFAULT NULL COMMENT 'order_id',
  `order_sn` varchar(255) DEFAULT NULL COMMENT 'order_sn',
  `consignee` varchar(100) DEFAULT NULL COMMENT '收货人',
  `consignee_tel` char(15) DEFAULT NULL COMMENT '收货人电话',
  `delivery_address` varchar(500) DEFAULT NULL COMMENT '配送地址',
  `order_comment` varchar(200) DEFAULT NULL COMMENT '订单备注',
  `payment_way` tinyint(1) DEFAULT NULL COMMENT '付款方式【 1:在线付款 2:货到付款】',
  `task_status` tinyint(2) DEFAULT NULL COMMENT '任务状态',
  `order_body` varchar(255) DEFAULT NULL COMMENT '订单描述',
  `tracking_no` char(30) DEFAULT NULL COMMENT '物流单号',
  `create_time` datetime DEFAULT NULL COMMENT 'create_time',
  `ware_id` bigint(20) DEFAULT NULL COMMENT '仓库id',
  `task_comment` varchar(500) DEFAULT NULL COMMENT '工作单备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存工作单';

/*Data for the table `wms_ware_order_task` */

/*Table structure for table `wms_ware_order_task_detail` */

DROP TABLE IF EXISTS `wms_ware_order_task_detail`;

CREATE TABLE `wms_ware_order_task_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(255) DEFAULT NULL COMMENT 'sku_name',
  `sku_num` int(11) DEFAULT NULL COMMENT '购买个数',
  `task_id` bigint(20) DEFAULT NULL COMMENT '工作单id',
  `ware_id` bigint(20) DEFAULT NULL COMMENT '仓库id',
  `lock_status` int(1) DEFAULT NULL COMMENT '1-已锁定  2-已解锁  3-扣减',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存工作单';

/*Data for the table `wms_ware_order_task_detail` */

/*Table structure for table `wms_ware_sku` */

DROP TABLE IF EXISTS `wms_ware_sku`;

CREATE TABLE `wms_ware_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `ware_id` bigint(20) DEFAULT NULL COMMENT '仓库id',
  `stock` int(11) DEFAULT NULL COMMENT '库存数',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku_name',
  `stock_locked` int(11) DEFAULT '0' COMMENT '锁定库存',
  PRIMARY KEY (`id`),
  KEY `sku_id` (`sku_id`) USING BTREE,
  KEY `ware_id` (`ware_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品库存';

/*Data for the table `wms_ware_sku` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


```





## 逆向工程

这里使用开源的[`renren-generate`](https://gitee.com/renrenio/renren-generator)完成基本的CRUD代码生成。

这里要先注解掉Spring Security的import和注解

```java
// import org.apache.shiro.authz.annotation.RequiresPermissions;
// @RequiresPermissions
```

使用的`ctrl+shift+R`的全局修改。

![image-20220726104149688](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220726104149688.png)

## 微服务

### 注册中心

`Spring Cloud`中的`Netflix`中的`Eureka`，用于服务发现。

### 配置中心

`Spring Cloud`中的`Spring Cloud`

### 网关

`Spring Cloud`中的`Netflix`中的`Zuul`。

我们采用Spring Cloud Alibaba

Nacos，注册，配置中心

![image-20220726115823127](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220726115823127.png)

Nacos默认集群启动，使用

```shell
startup.cmd -m standalone
```

进行单机启动。

Spring Boot、Spring Cloud、Spring Cloud Alibaba以及Nacos的版本一定要对应，这里参考的是[阿里巴巴的文档](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)。

![image-20220726150509418](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220726150509418.png)

![image-20220726150515151](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220726150515151.png)



![image-20220726151152044](C:\Users\16089\AppData\Roaming\Typora\typora-user-images\image-20220726151152044.png)



### Feign声明式远程调用

Feign是一个声明式的http客户端，整合了(Ribbon)负载均衡以及(Hystrix)服务熔断。

Feign其实是一种RPC（Remote Procedure Call），远程调用。

高版本的Feign弃用了Ribbon，因此要先排除，然后添加spring-cloud-starter-netflix-ribbon 和 spring-cloud-starter-loadbalancer依赖。

```yaml
<dependency>
   <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.netflix.ribbon</groupId>
            <artifactId>ribbon</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-loadbalancer</artifactId>
<version>2.2.9.RELEASE</version>
</dependency>

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
	<version>2.2.9.RELEASE</version>
</dependency>
```



Nacos也可以作为配置中心，动态的设置配置文件然后发布。优先使用配置中心的配置文件。

> **踩了个巨大的坑，nacos新建配置文件的时候，文件id也要包含文件后缀，不要去掉后缀！**



HTTP有以下方法：GET、POST、PUT、DELETE、OPTIONS、HEAD、TRACE、CONNECT

### 三级分类



### 云存储

OSS

| 中文      | 英文      | 说明                                                         |
| --------- | --------- | ------------------------------------------------------------ |
| 存储空间  | Bucket    | 存储空间是存储对象（Object）的容器，所有对象必须隶属于某个存储空间 |
| 对象/文件 | Object    | 对象是OSS最基本的存储单元，也被称为OSS的文件。对象由元信息（Object Meta）、用户数据（Data）和文件名（Key）组成。对象由存储空间内部唯一标识的`Key`来标识 |
| 地域      | Region    | 数据中心物理位置                                             |
| 访问域名  | Endpoint  | Endpoint 表示OSS对外服务的访问域名。OSS以HTTP RESTful API的形式对外提供服务，当访问不同地域的时候，需要不同的域名。通过内网和外网访问同一个地域所需要的域名也是不同的 |
| 访问密钥  | AccessKey | AccessKey，简称 AK，指的是访问身份验证中用到的AccessKeyId 和AccessKeySecret。OSS通过使用AccessKeyId 和AccessKeySecret对称加密的方法来验证某个请求的发送者身份。AccessKeyId用于标识用户，AccessKeySecret是用户用于加密签名字符串和OSS用来验证签名字符串的密钥，其中AccessKeySecret 必须保密。 |

之前为了安全器件，是用户把文件上传至应用服务器，然后上传OSS，如果把上传做在服务里面，那么每次上传都要请求应用服务器，很麻烦。

解决方式：浏览器从应用服务器获取签名，直接上传OSS。



### ElasticSearch

非结构化数据，由于其没有结构，所以最常见的就是顺序扫描，从前到后查询内容，其效率无疑是非常慢的。

另一个思路就是建立结构，使非结构化数据弄成结构化，然后对有结构的数据进行搜索，就可以实现快速的搜索。

从非结构化数据提取出的重新组织的信息，被称为索引。

ElasticSearch基于Apache的Lucene包。Lucene对于非结构数据的处理是将其倒排索引，得到词表以及倒排文件，倒排文件是存储在硬盘中，词表在内存中。

ES 的集群搭建很简单，不需要依赖第三方协调管理组件，自身内部就实现了集群的管理功能。

ES 集群由一个或多个 Elasticsearch 节点组成，每个节点配置相同的 cluster.name 即可加入集群，默认值为 “[elasticsearch](https://www.zhihu.com/search?q=elasticsearch&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"article"%2C"sourceId"%3A"529387057"})”。

确保不同的环境中使用不同的集群名称，否则最终会导致节点加入错误的集群。

一个 Elasticsearch 服务启动实例就是一个节点（Node）。节点通过 node.name 来设置节点名称，如果不设置则在启动时给节点分配一个随机通用唯一标识符作为名称。



## 开发过程中一些个人理解

SSM框架是一个web应用的完整开发框架，Spring简化了代码的开发流程、并且使得各层（业务持久层、服务层、控制层）清晰明了，Spring MVC对应控制层的部分，控制层将服务层提供的方法进行http接口化，将前端传回的http请求映射到对应的后端服务层方法中，并且以JSON格式返回请求数据。

Mybatis提供了对数据库进行便捷处理的方法（Mapper）。

