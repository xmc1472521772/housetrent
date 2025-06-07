package com.zpark.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

public class PermitPathMatcher {
    private final List<RequestMatcher> permitMatchers = new ArrayList<>();

    public void addPermitPaths(List<String> paths) {
        paths.forEach(path -> permitMatchers.add(new AntPathRequestMatcher(path)));
    }

    public boolean matches(HttpServletRequest request) {
        return permitMatchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
}
