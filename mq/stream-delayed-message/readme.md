# stream-delayed-message

## intro

这个项目主要是为了实验通过消息ttl和死信队列来实现动态批量的功能，这里主要讨论了消息的ttl是否有上限的要求，经过实验发现通过该方式实现的动态延时批量对于延迟时间是没有上限的限制的。