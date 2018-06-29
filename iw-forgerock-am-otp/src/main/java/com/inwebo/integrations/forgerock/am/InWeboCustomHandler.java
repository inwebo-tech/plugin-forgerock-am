package com.inwebo.integrations.forgerock.am;

import com.sun.identity.shared.debug.Debug;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static java.util.logging.Level.*;

public class InWeboCustomHandler extends Handler {

  private final Debug debug;

  public InWeboCustomHandler(final Debug debug) {
    this.debug = debug;
  }

  @Override
  public void publish(final LogRecord record) {
    final Level level = record.getLevel();
    final String message = record.getLoggerName() + "-" + record.getMessage();
    if (level.intValue() == SEVERE.intValue() && debug.errorEnabled()) {
      debug.error(message, record.getThrown());
    } else if (level.intValue() >= INFO.intValue() && debug.messageEnabled()) {
      debug.message(message, record.getThrown());
    } else if (level.intValue() >= WARNING.intValue() && debug.warningEnabled()) {
      debug.warning(message, record.getThrown());
    }
  }

  @Override
  public void flush() {
  }

  @Override
  public void close() throws SecurityException {
  }
}
