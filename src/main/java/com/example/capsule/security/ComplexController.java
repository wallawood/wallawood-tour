package com.example.capsule.security;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.annotations.Context;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.RequireCertificate;

@GeminiController
public class ComplexController {

  @Path("/complex")
  @RequireCertificate("Present a certificate to learn about custom security.")
  public GeminiResponse complex(@Context Grant grant) {
    Grant g = grant != null ? grant : Grant.deny();
    return GeminiResponse.success(
        "# Custom Security\n\n"
            + "Annotations only do AND. For anything else -- OR logic, conditional\n"
            + "checks, path-based rules -- use a @Preprocessor with the Authorization\n"
            + "class directly.\n\n"
            + "## The Authorization class\n"
            + "Same checks the annotations use, available as plain Java:\n"
            + "```\n"
            + "Authorization.requireAuthorized().check(grant)   // boolean\n"
            + "Authorization.requireClearance(3).check(grant)   // boolean\n"
            + "Authorization.requireScopes(\"write\").check(grant) // boolean\n"
            + "```\n\n"
            + "## OR logic in a Preprocessor\n"
            + "```\n"
            + "@Preprocessor(priority = 5)\n"
            + "public class OrGuard implements RequestInterceptor {\n"
            + "    static final Authorization CLEAR = Authorization.requireClearance(3);\n"
            + "    static final Authorization WRITE = Authorization.requireScopes(\"write\");\n"
            + "\n"
            + "    public Optional<GeminiResponse> intercept(RequestContext ctx) {\n"
            + "        URI uri = ctx.get(URI.class);\n"
            + "        if (!uri.getPath().startsWith(\"/complex/or\"))\n"
            + "            return Optional.empty();\n"
            + "\n"
            + "        Grant grant = ctx.get(Grant.class);\n"
            + "        if (CLEAR.check(grant) || WRITE.check(grant))\n"
            + "            return Optional.empty();  // allowed\n"
            + "\n"
            + "        return Optional.of(GeminiResponse\n"
            + "            .certificateNotAuthorized(\"Need clearance 3 OR write scope.\"));\n"
            + "    }\n"
            + "}\n"
            + "```\n\n"
            + "## Your grant\n"
            + "authorized = "
            + g.isAuthorized()
            + "\n"
            + "level      = "
            + g.level()
            + "\n"
            + "scopes     = "
            + g.scopes()
            + "\n\n"
            + "Try it (needs 'c' OR 's' in CN):\n"
            + "=> /complex/or [or] OR gate: clearance 3 OR write scope\n\n"
            + "=> /and-logic Stacking Annotations\n"
            + "=> / Home\n\n"
            + "---\n"
            + "Source: security/ComplexController.java -- demos the Authorization class for custom security logic.\n"
            + "OR gate logic lives in security/OrGuard.java -- a @Preprocessor that guards /complex/or.\n");
  }

  @Path("/complex/or")
  @RequireCertificate
  public GeminiResponse complexOr() {
    return GeminiResponse.success(
        "# [or] OR Gate Passed!\n\n"
            + "The preprocessor allowed you through because you have\n"
            + "clearance >= 3 OR the \"write\" scope (or both).\n\n"
            + "No annotation can express this. That's why the escape hatch\n"
            + "exists: plain Java in a @Preprocessor.\n\n"
            + "=> /complex Custom Security\n"
            + "=> / Home\n\n"
            + "---\n"
            + "Source: security/OrGuard.java -- a @Preprocessor implementing OR logic (clearance 3 OR write scope).\n");
  }
}
