package org.komamitsu.springdata.sqlite.example;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class IntegerToBooleanConverter implements Converter<Integer, Boolean> {
  public Boolean convert(Integer source) {
    System.out.println("USING CONVERTER");
    return Integer.valueOf(1).equals(source);
  }
}
