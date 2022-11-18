package com.vpavlov.services.api;

@FunctionalInterface
public interface TitleGenerator {

    String generateTitle(int count);
}
