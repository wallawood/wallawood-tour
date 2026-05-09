package com.example.capsule.security;

import io.gemboot.CertUtil;
import io.gemboot.Grant;
import io.gemboot.GeminiResponse;
import io.gemboot.RequestContext;
import io.gemboot.RequestInterceptor;
import io.gemboot.annotations.Preprocessor;

import java.security.cert.X509Certificate;
import java.util.Optional;

@Preprocessor(priority = 1)
public class Authority1 implements RequestInterceptor {

    @Override
    public Optional<GeminiResponse> intercept(RequestContext context) {
        X509Certificate cert = context.get(X509Certificate.class);
        if (cert == null) {
            return Optional.empty();
        }

        String cn = CertUtil.cn(cert).toLowerCase();
        Grant grant = Grant.none();

        if (cn.equalsIgnoreCase("a")) {
            grant = Grant.authorized();
        }

        if (cn.equalsIgnoreCase("c")) {
            grant = Grant.clearance(3);
        }

        if (cn.equalsIgnoreCase("s")) {
            grant = Grant.scopes("read", "write");
        }

        context.add(grant);
        return Optional.empty();
    }
}