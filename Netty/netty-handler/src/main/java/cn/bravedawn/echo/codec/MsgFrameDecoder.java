package cn.bravedawn.echo.codec;

import cn.bravedawn.echo.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 17:21
 */
public class MsgFrameDecoder extends LengthFieldBasedFrameDecoder {


    public MsgFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 6, 4, 0, true);
    }


    /**
     * 计算报文长度
     * @param buf
     * @param offset
     * @param length
     * @param order
     * @return
     */
    @Override
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
        byte[] bytes = new byte[length];
        buf.getBytes(offset, bytes);
        return Long.parseLong(ByteUtil.bytes2GBKStr(bytes));
    }
}
