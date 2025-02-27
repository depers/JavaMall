package cn.bravedawn.chapter17.handler.serverhandler;

import cn.bravedawn.chapter17.packet.group.CreateGroupResponsePacket;
import cn.bravedawn.chapter17.utils.IdUtil;
import cn.bravedawn.chapter17.utils.SessionUtil;
import cn.bravedawn.chapter17.packet.group.CreateGroupRequestPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : depers
 * @program : weixin-netty
 * @date : Created in 2024/6/11 11:42
 */
@Slf4j
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket requestPacket) throws Exception {
        List<String> userIdList = requestPacket.getUserIdList();

        List<String> userNameList = new ArrayList<>();

        // 创建一个 channel 分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        // 获取带加入群聊用户的 channel 和 userName
        for (String userId : userIdList) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }

        // 创建群聊结果的响应
        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setSuccess(true);
        responsePacket.setGroupId(IdUtil.randomId());
        responsePacket.setUserNameList(userNameList);

        // 给每个群聊客户都发送拉群通知
        channelGroup.writeAndFlush(responsePacket);

        log.info("创建群聊成功，id 为[{}}", responsePacket.getGroupId());
        log.info("群聊成员有：{}", responsePacket.getUserNameList());
    }
}
