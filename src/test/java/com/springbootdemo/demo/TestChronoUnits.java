package com.springbootdemo.demo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TestChronoUnits {
  public static void main(String[] args) {
    long between = ChronoUnit.MONTHS.between(LocalDate.of(2024, 1, 15), LocalDate.now());
    System.out.println(between);
  }
}
