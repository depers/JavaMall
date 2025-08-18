package cn.bravedawn.core;

public interface AbstractDeadLetterBlockingQueueConsumer {

    String getTopicPattern();

    void handleConsumer(PulsarMessage pulsarMessage);

    void exceptionHandle(Throwable e);

}
