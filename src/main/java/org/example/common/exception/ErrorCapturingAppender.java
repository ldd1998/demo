package org.example.common.exception;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorCapturingAppender extends AppenderBase<ILoggingEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCapturingAppender.class);

    @Override
    protected void append(ILoggingEvent event) {
        if (event.getLevel().levelInt >= ch.qos.logback.classic.Level.ERROR_INT) {
            // 捕捉 Error 级别的日志
            String errorMessage = event.getFormattedMessage();
            LOGGER.error("捕捉到Logback中的Error日志: {}", errorMessage);
            // 在这里你可以根据需要执行其他操作
        }
    }
}
