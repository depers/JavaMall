

create table `t_mq_record` (
  `id` bigint auto_increment comment '主键',
  `msg_content` text not null comment '消息内容',
  `try_count` int not null default '0' comment '重发次数',
  `status` tinyint not null default '0' comment '发送状态，0-发送中，1-发送成功，2-发送失败',
  `next_retry_time` datetime not null default current_timestamp comment '重发时间',
  `create_user` varchar(50) not null default '' comment '创建人',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_user` varchar(50) not null default '' comment '更新人',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`)
 )engine=innodb comment '消息消费记录表';