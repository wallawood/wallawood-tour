package com.example.capsule.security;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.annotations.Context;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.RequireAuthorized;
import io.github.wallawood.annotations.RequireCertificate;
import io.github.wallawood.annotations.RequireClearance;
import io.github.wallawood.annotations.RequireScopes;

@GeminiController
public class AndLogicController {

  @Path("/and-logic")
  @RequireCertificate("Present a certificate to learn about AND logic.")
  public GeminiResponse andLogic(@Context Grant grant) {
    Grant g = grant != null ? grant : Grant.deny();
    return GeminiResponse.success(
        "# Stacking Annotations\n\n"
            + "Put multiple annotations on one method. The framework ANDs them.\n"
            + "ALL must pass, or the request is rejected.\n\n"
            + "## Example\n"
            + "```\n"
            + "@RequireAuthorized\n"
            + "@RequireClearance(level = 3)\n"
            + "@RequireScopes(scopes = {\"read\", \"write\"})\n"
            + "public GeminiResponse topSecret() { ... }\n"
            + "```\n\n"
            + "This requires:\n"
            + "- grant.isAuthorized() == true\n"
            + "- grant.level() >= 3\n"
            + "- grant.scopes() contains \"read\" AND \"write\"\n\n"
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
            + "Try it (needs 'a' + 'c' + 's' in CN, e.g. CN=acs):\n"
            + "=> /and-logic/try [lock] Enter the triple-locked room\n\n"
            + "=> /scopes Scopes\n"
            + "=> /complex Next: Custom Security ->\n"
            + "=> / Home\n\n"
            + "---\n"
            + "Source: security/AndLogicController.java -- demos stacking multiple security annotations (AND logic).\n");
  }

  @Path("/and-logic/try")
  @RequireAuthorized(message = "Need authorized. Put 'a' in CN.")
  @RequireClearance(level = 3, message = "Need clearance 3. Put 'c' in CN.")
  @RequireScopes(
      scopes = {"read", "write"},
      message = "Need read+write scopes. Put 's' in CN.")
  public GeminiResponse andLogicTry() {
    return GeminiResponse.success(
        "# [lock] Triple Lock Passed!\n\n"
            + "All three annotations satisfied:\n"
            + "- @RequireAuthorized [ok]\n"
            + "- @RequireClearance(level = 3) [ok]\n"
            + "- @RequireScopes(scopes = {\"read\", \"write\"}) [ok]\n\n"
            + "=> /and-logic Stacking Annotations\n\n"
            + "---\n"
            + "Source: security/AndLogicController.java -- @RequireAuthorized, @RequireClearance, and @RequireScopes stack on this method.\n");
  }
}
