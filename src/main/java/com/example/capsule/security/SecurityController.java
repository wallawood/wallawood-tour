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
public class SecurityController {

    // === /grant â€” The Magic Object and Authority2 Pattern ===

    @Path("/grant")
    @RequireAuthorized(message = "Present a certificate to learn about Grants.")
    public GeminiResponse grant(@Context Grant grant) {
        return GeminiResponse.success(
                "# The Magic Object and Authority2 Pattern\n\n"
                + "A Grant is what the framework checks when you protect a route.\n"
                + "It answers: \"What permissions does this visitor have?\"\n\n"
                + "## The Pattern\n"
                + "1. Client connects with a TLS certificate\n"
                + "2. Your @Preprocessor (the Authority2) inspects the cert\n"
                + "3. The Authority2 builds a Grant and adds it to the request\n"
                + "4. The framework checks the Grant against your annotations\n\n"
                + "## Three Dimensions (all independent)\n"
                + "```\n"
                + "Grant.authorized()       â†’ a boolean flag\n"
                + "Grant.clearance(3)       â†’ a numeric level\n"
                + "Grant.scopes(\"r\", \"w\")   â†’ a set of permissions\n"
                + "```\n\n"
                + "## Your Grant right now\n"
                + "authorized = " + grant.isAuthorized() + "\n"
                + "level      = " + grant.level() + "\n"
                + "scopes     = " + grant.scopes() + "\n\n"
                + "## The Authority2 (your @Preprocessor)\n"
                + "```\n"
                + "@Preprocessor(priority = 0)\n"
                + "public class Authority2 implements RequestInterceptor {\n"
                + "    public Optional<GeminiResponse> intercept(RequestContext ctx) {\n"
                + "        X509Certificate cert = ctx.get(X509Certificate.class);\n"
                + "        String cn = /* extract CN from cert */;\n"
                + "        ctx.add(Grant.builder()\n"
                + "            .authorized(true)\n"
                + "            .clearance(3)\n"
                + "            .addScope(\"read\")\n"
                + "            .build());\n"
                + "        return Optional.empty();\n"
                + "    }\n"
                + "}\n"
                + "```\n\n"
                + "In this demo, your cert CN controls what you get:\n"
                + "  'a' â†’ authorized, 'c' â†’ clearance 3, 's' â†’ scopes read+write\n\n"
                + "=> /guard Next: The Guard Pattern â†’\n"
                + "=> / â† Home\n");
    }

    // === /guard â€” Guard Pattern ===

    @Path("/guard")
    @RequireCertificate("Present a certificate to learn about Guards.")
    public GeminiResponse guard(@Context Grant grant) {
        String status = grant.isAuthorized() ? "âœ… You ARE authorized" : "âŒ You are NOT authorized";
        return GeminiResponse.success(
                "# Guard Pattern\n\n"
                + "A Guard is an annotation that blocks a request if the Grant\n"
                + "doesn't meet the requirement. No Grant? Blocked.\n\n"
                + "## @RequireAuthorized\n"
                + "The simplest guard. Checks: does grant.isAuthorized() == true?\n\n"
                + "```\n"
                + "@RequireAuthorized\n"
                + "public GeminiResponse secret() { ... }\n"
                + "```\n\n"
                + "## What happens\n"
                + "- No certificate â†’ status 60 (Client Certificate Required)\n"
                + "- Certificate but grant fails â†’ status 61 (Not Authorized)\n"
                + "- Grant passes â†’ handler runs\n\n"
                + "## Your status\n"
                + status + "\n\n"
                + "Try it:\n"
                + "=> /guard/try ðŸšª Enter the guarded room (needs 'a' in CN)\n\n"
                + "=> /grant â† The Magic Object\n"
                + "=> /clearance Next: Clearance Levels â†’\n"
                + "=> / â† Home\n");
    }

    // === /clearance â€” User, Moderator, Admin ===

    @Path("/clearance")
    @RequireCertificate("Present a certificate to learn about Clearance.")
    public GeminiResponse clearance(@Context Grant grant) {
        return GeminiResponse.success(
                "# User, Moderator, Admin\n\n"
                + "@RequireClearance checks a numeric level on the Grant.\n"
                + "Higher level = more access. Think: user=1, mod=2, admin=3.\n\n"
                + "## How it works\n"
                + "```\n"
                + "// Grant side â€” what the user has:\n"
                + "Grant.clearance(3)\n\n"
                + "// Annotation side â€” what the route requires:\n"
                + "@RequireClearance(level = 3)\n"
                + "public GeminiResponse adminPanel() { ... }\n"
                + "```\n\n"
                + "The check: grant.level() >= required level.\n"
                + "Level 5 passes a check for level 3. Level 2 does not.\n\n"
                + "## Your level: " + grant.level() + "\n\n"
                + "Try these doors:\n"
                + "=> /clearance/user ðŸ‘¤ User area (level >= 1)\n"
                + "=> /clearance/mod ðŸ›¡ï¸ Moderator area (level >= 2)\n"
                + "=> /clearance/admin ðŸ‘‘ Admin area (level >= 3) â€” needs 'c' in CN\n\n"
                + "## Key point\n"
                + "Clearance does NOT imply @RequireAuthorized, and vice versa.\n"
                + "They are separate dimensions. Stack both if you need both.\n\n"
                + "=> /guard â† Guard Pattern\n"
                + "=> /scopes Next: Scopes â†’\n"
                + "=> / â† Home\n");
    }

    @Path("/guard/try")
    @RequireAuthorized(message = "Put 'a' in your certificate CN to pass.")
    public GeminiResponse guardTry() {
        return GeminiResponse.success(
                "# ðŸšª You're in!\n\n"
                        + "@RequireAuthorized passed. Your grant has isAuthorized() = true.\n\n"
                        + "=> /guard â† Guard Pattern\n");
    }

    @Path("/clearance/user")
    @RequireClearance(level = 1, message = "Need clearance level 1. Put 'c' in your CN.")
    public GeminiResponse clearanceUser() {
        return GeminiResponse.success(
                "# ðŸ‘¤ User Area\n\n"
                + "@RequireClearance(level = 1) â€” you passed.\n\n"
                + "=> /clearance â† Clearance Levels\n");
    }

    @Path("/clearance/mod")
    @RequireClearance(level = 2, message = "Need clearance level 2. Put 'c' in your CN.")
    public GeminiResponse clearanceMod() {
        return GeminiResponse.success(
                "# ðŸ›¡ï¸ Moderator Area\n\n"
                + "@RequireClearance(level = 2) â€” you passed.\n\n"
                + "=> /clearance â† Clearance Levels\n");
    }

    @Path("/clearance/admin")
    @RequireClearance(level = 3, message = "Need clearance level 3. Put 'c' in your CN.")
    public GeminiResponse clearanceAdmin() {
        return GeminiResponse.success(
                "# ðŸ‘‘ Admin Area\n\n"
                + "@RequireClearance(level = 3) â€” you passed.\n\n"
                + "=> /clearance â† Clearance Levels\n");
    }

    // === /scopes â€” Read, Write, Delete ===

    @Path("/scopes")
    @RequireCertificate("Present a certificate to learn about Scopes.")
    public GeminiResponse scopes(@Context Grant grant) {
        return GeminiResponse.success(
                "# Read, Write, Delete\n\n"
                + "@RequireScopes checks that the Grant contains specific permission strings.\n"
                + "All listed scopes must be present â€” it's AND within the set.\n\n"
                + "## How it works\n"
                + "```\n"
                + "// Grant side â€” what the user has:\n"
                + "Grant.scopes(\"read\", \"write\")\n\n"
                + "// Annotation side â€” what the route requires:\n"
                + "@RequireScopes(scopes = {\"read\", \"write\"})\n"
                + "public GeminiResponse editDoc() { ... }\n"
                + "```\n\n"
                + "The check: grant.scopes() must contain ALL required scopes.\n\n"
                + "## Your scopes: " + grant.scopes() + "\n\n"
                + "Try these:\n"
                + "=> /scopes/read ðŸ“– Read (needs \"read\" scope)\n"
                + "=> /scopes/write âœï¸ Write (needs \"write\" scope)\n"
                + "=> /scopes/delete ðŸ—‘ï¸ Delete (needs \"delete\" scope) â€” you don't have this!\n\n"
                + "## Key point\n"
                + "Scopes do NOT imply authorized or clearance.\n"
                + "A user can have write permission without being 'authorized'.\n\n"
                + "=> /clearance â† Clearance Levels\n"
                + "=> /and-logic Next: Stacking Annotations â†’\n"
                + "=> / â† Home\n");
    }

    @Path("/scopes/read")
    @RequireScopes(scopes = "read", message = "Need 'read' scope. Put 's' in your CN.")
    public GeminiResponse scopesRead() {
        return GeminiResponse.success(
                "# ðŸ“– Read Access\n\n"
                + "@RequireScopes(scopes = \"read\") â€” you passed.\n\n"
                + "=> /scopes â† Scopes\n");
    }

    @Path("/scopes/write")
    @RequireScopes(scopes = "write", message = "Need 'write' scope. Put 's' in your CN.")
    public GeminiResponse scopesWrite() {
        return GeminiResponse.success(
                "# âœï¸ Write Access\n\n"
                + "@RequireScopes(scopes = \"write\") â€” you passed.\n\n"
                + "=> /scopes â† Scopes\n");
    }

    @Path("/scopes/delete")
    @RequireScopes(scopes = "delete", message = "Need 'delete' scope. Nobody has this in the demo!")
    public GeminiResponse scopesDelete() {
        return GeminiResponse.success(
                "# ðŸ—‘ï¸ Delete Access\n\n"
                + "@RequireScopes(scopes = \"delete\") â€” you passed. How?!\n\n"
                + "=> /scopes â† Scopes\n");
    }

    // === /and-logic â€” Stacking annotations ===

    @Path("/and-logic")
    @RequireCertificate("Present a certificate to learn about AND logic.")
    public GeminiResponse andLogic(@Context Grant grant) {
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
                + "authorized = " + grant.isAuthorized() + "\n"
                + "level      = " + grant.level() + "\n"
                + "scopes     = " + grant.scopes() + "\n\n"
                + "Try it (needs 'a' + 'c' + 's' in CN, e.g. CN=acs):\n"
                + "=> /and-logic/try ðŸ§± Enter the triple-locked room\n\n"
                + "=> /scopes â† Scopes\n"
                + "=> /complex Next: Custom Security â†’\n"
                + "=> / â† Home\n");
    }

    @Path("/and-logic/try")
    @RequireAuthorized(message = "Need authorized. Put 'a' in CN.")
    @RequireClearance(level = 3, message = "Need clearance 3. Put 'c' in CN.")
    @RequireScopes(scopes = {"read", "write"}, message = "Need read+write scopes. Put 's' in CN.")
    public GeminiResponse andLogicTry() {
        return GeminiResponse.success(
                "# ðŸ§± Triple Lock Passed!\n\n"
                + "All three annotations satisfied:\n"
                + "- @RequireAuthorized âœ…\n"
                + "- @RequireClearance(level = 3) âœ…\n"
                + "- @RequireScopes(scopes = {\"read\", \"write\"}) âœ…\n\n"
                + "=> /and-logic â† Stacking Annotations\n");
    }

    // === /complex â€” Custom security with Authorization + Guard Pattern ===

    @Path("/complex")
    @RequireCertificate("Present a certificate to learn about custom security.")
    public GeminiResponse complex(@Context Grant grant) {
        return GeminiResponse.success(
                "# Custom Security\n\n"
                + "Annotations only do AND. For anything else â€” OR logic, conditional\n"
                + "checks, path-based rules â€” use a @Preprocessor with the Authorization\n"
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
                + "authorized = " + grant.isAuthorized() + "\n"
                + "level      = " + grant.level() + "\n"
                + "scopes     = " + grant.scopes() + "\n\n"
                + "Try it (needs 'c' OR 's' in CN):\n"
                + "=> /complex/or ðŸ”€ OR gate: clearance 3 OR write scope\n\n"
                + "=> /and-logic â† Stacking Annotations\n"
                + "=> / â† Home\n");
    }

    @Path("/complex/or")
    @RequireCertificate
    public GeminiResponse complexOr() {
        return GeminiResponse.success(
                "# ðŸ”€ OR Gate Passed!\n\n"
                + "The preprocessor allowed you through because you have\n"
                + "clearance >= 3 OR the \"write\" scope (or both).\n\n"
                + "No annotation can express this. That's why the escape hatch\n"
                + "exists: plain Java in a @Preprocessor.\n\n"
                + "=> /complex â† Custom Security\n"
                + "=> / â† Home\n");
    }
}
