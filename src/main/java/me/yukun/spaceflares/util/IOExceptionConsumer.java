package me.yukun.spaceflares.util;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionConsumer<T> {

  void accept(T t) throws IOException;
}
