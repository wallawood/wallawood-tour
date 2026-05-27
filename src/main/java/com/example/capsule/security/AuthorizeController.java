package com.example.capsule.security;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.annotations.Context;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.RequireAuthorized;

@GeminiController
public class AuthorizeController {

  @Path("/authorize")
  public GeminiResponse authorize(@Context Grant grant) {
    Grant g = grant != null ? grant : Grant.deny();
    String status = g.isAuthorized() ? "[ok] You ARE authorized" : "[--] You are NOT authorized";
    return GeminiResponse.success(
        "# The Simplest Grant\n\n"
            + "This Grant is a simple boolean check.\n"
            + "No Grant? Blocked.\n\n"
            + "## @RequireAuthorized\n"
            + "Checks: does grant.isAuthorized() == true?\n\n"
            + "```\n"
            + "@RequireAuthorized\n"
            + "public GeminiResponse secret() { ... }\n"
            + "```\n\n"
            + "## What happens\n"
            + "- No grant (or grant fails check) -> status 61 (Not Authorized)\n"
            + "- Grant passes -> handler runs\n"
            + "- Stack @RequireCertificate to get status 60 for cert-less clients\n\n"
            + "## Your status\n"
            + status
            + "\n\n"
            + "Try it:\n"
            + "=> /authorize/try [enter] Enter the guarded room (needs 'a' in CN)\n\n"
            + "=> /grant The Grant Object\n"
            + "=> /clearance Next: Clearance Levels ->\n"
            + "=> / Home\n\n"
            + "---\n"
            + "Source: security/AuthorizeController.java -- demos @RequireAuthorized for basic boolean auth checks.\n");
  }

  @Path("/authorize/try")
  @RequireAuthorized(message = "Put 'a' in your certificate CN to pass.")
  public GeminiResponse authorizeTry() {
    return GeminiResponse.success(
        "# [enter] You're in!\n\n"
            + "@RequireAuthorized passed. Your grant has isAuthorized() = true.\n\n"
            + "=> /authorize The Simplest Grant\n\n"
            + "---\n"
            + "Source: security/AuthorizeController.java -- @RequireAuthorized guards this method.\n");
  }
}
