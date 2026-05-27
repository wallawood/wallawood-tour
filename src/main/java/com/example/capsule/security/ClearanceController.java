package com.example.capsule.security;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.annotations.Context;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.RequireCertificate;
import io.github.wallawood.annotations.RequireClearance;

@GeminiController
public class ClearanceController {

  @Path("/clearance")
  @RequireCertificate("Present a certificate to learn about Clearance.")
  public GeminiResponse clearance(@Context Grant grant) {
    Grant g = grant != null ? grant : Grant.deny();
    return GeminiResponse.success(
        "# User, Moderator, Admin\n\n"
            + "@RequireClearance checks a numeric level on the Grant.\n"
            + "Higher level = more access. Think: user=1, mod=2, admin=3.\n\n"
            + "## How it works\n"
            + "```\n"
            + "// Grant side -- what the user has:\n"
            + "Grant.clearance(3)\n\n"
            + "// Annotation side -- what the route requires:\n"
            + "@RequireClearance(level = 3)\n"
            + "public GeminiResponse adminPanel() { ... }\n"
            + "```\n\n"
            + "The check: grant.level() >= required level.\n"
            + "Level 5 passes a check for level 3. Level 2 does not.\n\n"
            + "## Your level: "
            + g.level()
            + "\n\n"
            + "Try these doors:\n"
            + "=> /clearance/user [user] User area (level >= 1)\n"
            + "=> /clearance/mod [shield] Moderator area (level >= 2)\n"
            + "=> /clearance/admin [admin] Admin area (level >= 3) -- needs 'c' in CN\n\n"
            + "## Key point\n"
            + "Clearance does NOT imply @RequireAuthorized, and vice versa.\n"
            + "They are separate dimensions. Stack both if you need both.\n\n"
            + "=> /authorize The Simplest Grant\n"
            + "=> /scopes Next: Scopes ->\n"
            + "=> / Home\n\n"
            + "---\n"
            + "Source: security/ClearanceController.java -- demos @RequireClearance for numeric level-based access.\n");
  }

  @Path("/clearance/user")
  @RequireClearance(level = 1, message = "Need clearance level 1. Put 'c' in your CN.")
  public GeminiResponse clearanceUser() {
    return GeminiResponse.success(
        "# [user] User Area\n\n"
            + "@RequireClearance(level = 1) -- you passed.\n\n"
            + "=> /clearance Clearance Levels\n\n"
            + "---\n"
            + "Source: security/ClearanceController.java -- @RequireClearance(level = 1) guards this method.\n");
  }

  @Path("/clearance/mod")
  @RequireClearance(level = 2, message = "Need clearance level 2. Put 'c' in your CN.")
  public GeminiResponse clearanceMod() {
    return GeminiResponse.success(
        "# [shield] Moderator Area\n\n"
            + "@RequireClearance(level = 2) -- you passed.\n\n"
            + "=> /clearance Clearance Levels\n\n"
            + "---\n"
            + "Source: security/ClearanceController.java -- @RequireClearance(level = 2) guards this method.\n");
  }

  @Path("/clearance/admin")
  @RequireClearance(level = 3, message = "Need clearance level 3. Put 'c' in your CN.")
  public GeminiResponse clearanceAdmin() {
    return GeminiResponse.success(
        "# [admin] Admin Area\n\n"
            + "@RequireClearance(level = 3) -- you passed.\n\n"
            + "=> /clearance Clearance Levels\n\n"
            + "---\n"
            + "Source: security/ClearanceController.java -- @RequireClearance(level = 3) guards this method.\n");
  }
}
