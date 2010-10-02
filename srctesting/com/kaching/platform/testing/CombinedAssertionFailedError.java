package com.kaching.platform.testing;

import com.kaching.platform.common.Errors;

/**
 * Thrown when an assertion failed for multiple reasons.
 */
public class CombinedAssertionFailedError extends AssertionError {

  private static final long serialVersionUID = 5967202290583940192L;

  private final String message;
  private final Errors errors;

  public CombinedAssertionFailedError() {
    this(null);
  }

  public CombinedAssertionFailedError(String message) {
    this.message = message;
    this.errors = new Errors();
  }

  public void addError(String error) {
    errors.addMessage(error);
  }

  @Override
  public String getMessage() {
    return toString();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (message != null) {
      builder.append(message);
    }
    if (0 < errors.size()) {
      if (message != null) {
        builder.append(':');
        builder.append('\n');
      }
      builder.append(errors.toString());
    }
    return builder.toString();
  }

  /**
   * Throws this {@link CombinedAssertionFailedError} if any error was
   * encountered.
   */
  public void throwIfHasErrors() {
    if (0 < errors.size()) {
      throw this;
    }
  }

}