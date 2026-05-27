package com.example.capsule.security;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.Grant;
import io.github.wallawood.annotations.Context;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.RequireCertificate;
import io.github.wallawood.annotations.RequireScopes;

@GeminiController
public class ScopesController {

  @Path("/scopes")
  @RequireCertificate("Present a certificate to learn about Scopes.")
  public GeminiResponse scopes(@Context Grant grant) {
    Grant g = grant != null ? grant : Grant.deny();
    return GeminiResponse.success(
        "# Read, Write, Delete\n\n"
            + "@RequireScopes checks that the Grant contains specific permission strings.\n"
            + "All listed scopes must be present -- it's AND within the set.\n\n"
            + "## How it works\n"
            + "```\n"
            + "// Grant side -- what the user has:\n"
            + "Grant.scopes(\"read\", \"write\")\n\n"
            + "// Annotation side -- what the route requires:\n"
            + "@RequireScopes(scopes = {\"read\", \"write\"})\n"
            + "public GeminiResponse editDoc() { ... }\n"
            + "```\n\n"
            + "The check: grant.scopes() must contain ALL required scopes.\n\n"
            + "## Your scopes: "
            + g.scopes()
            + "\n\n"
            + "Try these:\n"
            + "=> /scopes/read [read] Read (needs \"read\" scope)\n"
            + "=> /scopes/write [write] Write (needs \"write\" scope)\n"
            + "=> /scopes/delete [delete] Delete (needs \"delete\" scope) -- you don't have this!\n\n"
            + "## Key point\n"
            + "Scopes do NOT imply authorized or clearance.\n"
            + "A user can have write permission without being 'authorized'.\n\n"
            + "=> /clearance Clearance Levels\n"
            + "=> /and-logic Next: Stacking Annotations ->\n"
            + "=> / Home\n\n"
            + "---\n"
            + "Source: security/ScopesController.java -- demos @RequireScopes for string-based permission checks.\n");
  }

  @Path("/scopes/read")
  @RequireScopes(scopes = "read", message = "Need 'read' scope. Put 's' in your CN.")
  public GeminiResponse scopesRead() {
    return GeminiResponse.success(
        "# [read] Read Access\n\n"
            + "@RequireScopes(scopes = \"read\") -- you passed.\n\n"
            + "=> /scopes Scopes\n\n"
            + "---\n"
            + "Source: security/ScopesController.java -- @RequireScopes(scopes = \"read\") guards this method.\n");
  }

  @Path("/scopes/write")
  @RequireScopes(scopes = "write", message = "Need 'write' scope. Put 's' in your CN.")
  public GeminiResponse scopesWrite() {
    return GeminiResponse.success(
        "# [write] Write Access\n\n"
            + "@RequireScopes(scopes = \"write\") -- you passed.\n\n"
            + "=> /scopes Scopes\n\n"
            + "---\n"
            + "Source: security/ScopesController.java -- @RequireScopes(scopes = \"write\") guards this method.\n");
  }

  @Path("/scopes/delete")
  @RequireScopes(scopes = "delete", message = "Need 'delete' scope. Nobody has this in the demo!")
  public GeminiResponse scopesDelete() {
    return GeminiResponse.success(
        "# [delete] Delete Access\n\n"
            + "@RequireScopes(scopes = \"delete\") -- you passed. How?!\n\n"
            + "=> /scopes Scopes\n\n"
            + "---\n"
            + "Source: security/ScopesController.java -- @RequireScopes(scopes = \"delete\") guards this method.\n");
  }
}
