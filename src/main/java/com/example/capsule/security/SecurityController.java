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

        @Path("/authorize")
        public GeminiResponse guard(@Context Grant grant) {
                String status = grant.isAuthorized() ? "[ok] You ARE authorized" : "[--] You are NOT authorized";
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
                                                + status + "\n\n"
                                                + "Try it:\n"
                                                + "=> /guard/try [enter] Enter the guarded room (needs 'a' in CN)\n\n"
                                                + "=> /grant The Grant Object\n"
                                                + "=> /clearance Next: Clearance Levels ->\n"
                                                + "=> / Home\n");
        }

        // === /clearance -- User, Moderator, Admin ===

        @Path("/clearance")
        @RequireCertificate("Present a certificate to learn about Clearance.")
        public GeminiResponse clearance(@Context Grant grant) {
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
                                                + "## Your level: " + grant.level() + "\n\n"
                                                + "Try these doors:\n"
                                                + "=> /clearance/user [user] User area (level >= 1)\n"
                                                + "=> /clearance/mod [shield] Moderator area (level >= 2)\n"
                                                + "=> /clearance/admin [admin] Admin area (level >= 3) -- needs 'c' in CN\n\n"
                                                + "## Key point\n"
                                                + "Clearance does NOT imply @RequireAuthorized, and vice versa.\n"
                                                + "They are separate dimensions. Stack both if you need both.\n\n"
                                                + "=> /guard Guard Pattern\n"
                                                + "=> /scopes Next: Scopes ->\n"
                                                + "=> / Home\n");
        }

        @Path("/guard/try")
        @RequireAuthorized(message = "Put 'a' in your certificate CN to pass.")
        public GeminiResponse guardTry() {
                return GeminiResponse.success(
                                "# [enter] You're in!\n\n"
                                                + "@RequireAuthorized passed. Your grant has isAuthorized() = true.\n\n"
                                                + "=> /guard Guard Pattern\n");
        }

        @Path("/clearance/user")
        @RequireClearance(level = 1, message = "Need clearance level 1. Put 'c' in your CN.")
        public GeminiResponse clearanceUser() {
                return GeminiResponse.success(
                                "# [user] User Area\n\n"
                                                + "@RequireClearance(level = 1) -- you passed.\n\n"
                                                + "=> /clearance Clearance Levels\n");
        }

        @Path("/clearance/mod")
        @RequireClearance(level = 2, message = "Need clearance level 2. Put 'c' in your CN.")
        public GeminiResponse clearanceMod() {
                return GeminiResponse.success(
                                "# [shield] Moderator Area\n\n"
                                                + "@RequireClearance(level = 2) -- you passed.\n\n"
                                                + "=> /clearance Clearance Levels\n");
        }

        @Path("/clearance/admin")
        @RequireClearance(level = 3, message = "Need clearance level 3. Put 'c' in your CN.")
        public GeminiResponse clearanceAdmin() {
                return GeminiResponse.success(
                                "# [admin] Admin Area\n\n"
                                                + "@RequireClearance(level = 3) -- you passed.\n\n"
                                                + "=> /clearance Clearance Levels\n");
        }

        // === /scopes -- Read, Write, Delete ===

        @Path("/scopes")
        @RequireCertificate("Present a certificate to learn about Scopes.")
        public GeminiResponse scopes(@Context Grant grant) {
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
                                                + "## Your scopes: " + grant.scopes() + "\n\n"
                                                + "Try these:\n"
                                                + "=> /scopes/read [read] Read (needs \"read\" scope)\n"
                                                + "=> /scopes/write [write] Write (needs \"write\" scope)\n"
                                                + "=> /scopes/delete [delete] Delete (needs \"delete\" scope) -- you don't have this!\n\n"
                                                + "## Key point\n"
                                                + "Scopes do NOT imply authorized or clearance.\n"
                                                + "A user can have write permission without being 'authorized'.\n\n"
                                                + "=> /clearance Clearance Levels\n"
                                                + "=> /and-logic Next: Stacking Annotations ->\n"
                                                + "=> / Home\n");
        }

        @Path("/scopes/read")
        @RequireScopes(scopes = "read", message = "Need 'read' scope. Put 's' in your CN.")
        public GeminiResponse scopesRead() {
                return GeminiResponse.success(
                                "# [read] Read Access\n\n"
                                                + "@RequireScopes(scopes = \"read\") -- you passed.\n\n"
                                                + "=> /scopes Scopes\n");
        }

        @Path("/scopes/write")
        @RequireScopes(scopes = "write", message = "Need 'write' scope. Put 's' in your CN.")
        public GeminiResponse scopesWrite() {
                return GeminiResponse.success(
                                "# [write] Write Access\n\n"
                                                + "@RequireScopes(scopes = \"write\") -- you passed.\n\n"
                                                + "=> /scopes Scopes\n");
        }

        @Path("/scopes/delete")
        @RequireScopes(scopes = "delete", message = "Need 'delete' scope. Nobody has this in the demo!")
        public GeminiResponse scopesDelete() {
                return GeminiResponse.success(
                                "# [delete] Delete Access\n\n"
                                                + "@RequireScopes(scopes = \"delete\") -- you passed. How?!\n\n"
                                                + "=> /scopes Scopes\n");
        }

        // === /and-logic -- Stacking annotations ===

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
                                                + "=> /and-logic/try [lock] Enter the triple-locked room\n\n"
                                                + "=> /scopes Scopes\n"
                                                + "=> /complex Next: Custom Security ->\n"
                                                + "=> / Home\n");
        }

        @Path("/and-logic/try")
        @RequireAuthorized(message = "Need authorized. Put 'a' in CN.")
        @RequireClearance(level = 3, message = "Need clearance 3. Put 'c' in CN.")
        @RequireScopes(scopes = { "read", "write" }, message = "Need read+write scopes. Put 's' in CN.")
        public GeminiResponse andLogicTry() {
                return GeminiResponse.success(
                                "# [lock] Triple Lock Passed!\n\n"
                                                + "All three annotations satisfied:\n"
                                                + "- @RequireAuthorized [ok]\n"
                                                + "- @RequireClearance(level = 3) [ok]\n"
                                                + "- @RequireScopes(scopes = {\"read\", \"write\"}) [ok]\n\n"
                                                + "=> /and-logic Stacking Annotations\n");
        }

        // === /complex -- Custom security with Authorization + Guard Pattern ===

        @Path("/complex")
        @RequireCertificate("Present a certificate to learn about custom security.")
        public GeminiResponse complex(@Context Grant grant) {
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
                                                + "authorized = " + grant.isAuthorized() + "\n"
                                                + "level      = " + grant.level() + "\n"
                                                + "scopes     = " + grant.scopes() + "\n\n"
                                                + "Try it (needs 'c' OR 's' in CN):\n"
                                                + "=> /complex/or [or] OR gate: clearance 3 OR write scope\n\n"
                                                + "=> /and-logic Stacking Annotations\n"
                                                + "=> / Home\n");
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
                                                + "=> / Home\n");
        }
}
