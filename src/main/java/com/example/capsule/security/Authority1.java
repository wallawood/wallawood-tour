package com.example.capsule.security;

import io.github.wallawood.CertUtil;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.RequestContext;
import io.github.wallawood.RequestInterceptor;
import io.github.wallawood.annotations.Preprocessor;
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

    String cn = CertUtil.name(cert).toLowerCase();
    Grant grant = Grant.deny();

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
