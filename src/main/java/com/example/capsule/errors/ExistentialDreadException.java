package com.example.capsule.errors;

public class ExistentialDreadException extends RuntimeException {
  public ExistentialDreadException() {
    super("I'm a server. I serve pages. But who serves me?");
  }
}
