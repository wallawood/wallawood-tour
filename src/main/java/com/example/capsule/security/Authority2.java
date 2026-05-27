package com.example.capsule.security;

import io.github.wallawood.CertUtil;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.RequestContext;
import io.github.wallawood.RequestInterceptor;
import io.github.wallawood.annotations.Preprocessor;
import java.security.cert.X509Certificate;
import java.util.Optional;

@Preprocessor(priority = 2)
public class Authority2 implements RequestInterceptor {
  @Override
  public Optional<GeminiResponse> intercept(RequestContext context) {
    if (context.get(Grant.class) != null) {
      return Optional.empty();
    }

    X509Certificate cert = context.get(X509Certificate.class);
    if (cert == null) {
      return Optional.empty();
    }

    String cn = CertUtil.name(cert).toLowerCase();
    Grant.Builder builder = Grant.builder();

    if (cn.contains("a")) {
      builder.authorized(true);
    }

    if (cn.contains("c")) {
      builder.clearance(3);
    }

    if (cn.contains("s")) {
      builder.addScopes("read", "write");
    }

    context.add(builder.build());
    return Optional.empty();
  }
}
