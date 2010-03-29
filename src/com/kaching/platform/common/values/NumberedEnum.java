/**
 * Copyright 2009 KaChing Group Inc. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.kaching.platform.common.values;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapMaker;
import com.google.common.collect.ImmutableMap.Builder;
import com.kaching.platform.common.functional.Thunk;

/**
 * Utility class to help with numbered enums. A numbered enum is one that
 * implements {@link NumberedValue}.
 */
public class NumberedEnum {

  private static ConcurrentMap<Class<?>, Thunk<Map<Integer, Enum<?>>>> mappings =
      new MapMaker().makeMap();

  /**
   * Returns the enum constant of the specified enum type with the specified
   * number. (This function is similar to {@link Enum#valueOf(Class, String)}.)
   * 
   * @throws IllegalArgumentException if no enum match the provided number
   * @throws NullPointerException if the {@code value} is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E extends Enum<E> & NumberedValue> E valueOf(final Class<E> type, int number) {
    checkNotNull(type);
    mappings.putIfAbsent(
        type,
        new Thunk<Map<Integer,Enum<?>>>() {
          @Override
          protected Map<Integer, Enum<?>> compute() {
            try {
              Builder<Integer, Enum<?>> builder = ImmutableMap.<Integer, Enum<?>> builder();
              E[] values = (E[]) type.getMethod("values").invoke(null);
              for (E value : values) {
                builder.put(value.getNumber(), value);
              }
              return builder.build();
            } catch (Exception e) {
              throw new IllegalStateException();
            }
          }
        });
    E value = (E) mappings.get(type).get().get(number);
    checkArgument(value != null);
    return value;
  }
  
}