package com.example.capsule.security;

import io.github.wallawood.Authorization;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.RequestContext;
import io.github.wallawood.RequestInterceptor;
import io.github.wallawood.annotations.Preprocessor;
import java.net.URI;
import java.util.Optional;

/**
 * Demonstrates OR logic in a preprocessor. Allows access to /security/or-demo if the user has
 * clearance >= 3 OR scope "write".
 */
@Preprocessor(priority = 5)
public class OrGuard implements RequestInterceptor {

  private static final Authorization CLEARANCE = Authorization.requireClearance(3);
  private static final Authorization WRITER = Authorization.requireScopes("write");

  @Override
  public Optional<GeminiResponse> intercept(RequestContext context) {
    URI uri = context.get(URI.class);
    if (uri == null || !uri.getPath().startsWith("/complex/or")) {
      return Optional.empty();
    }

    Grant grant = context.get(Grant.class);
    if (CLEARANCE.check(grant) || WRITER.check(grant)) {
      return Optional.empty();
    }

    return Optional.of(
        GeminiResponse.certificateNotAuthorized(
            "Need clearance 3 OR write scope. Put 'c' or 's' in your cert CN."));
  }
}
