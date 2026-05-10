package com.example.capsule.context;

import io.github.wallawood.CertUtil;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.Context;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.RequireCertificate;

import java.net.URI;
import java.security.cert.X509Certificate;

@GeminiController
public class CertificateController {

    @Path("/whoami")
    @RequireCertificate("Your attendance is requested.")
    public GeminiResponse showCertificate(@Context URI uri, @Context X509Certificate cert) {

        String name = CertUtil.cn(cert);
        String id = CertUtil.fingerprint(cert);
        String issuer = cert.getIssuerX500Principal().getName().replaceFirst("^CN=", "");

        return GeminiResponse.success(
                "# Who Am I?\n\n"
                + "## Your Request\n"
                + "URI: " + uri + "\n"
                + "Path: " + uri.getPath() + "\n\n"
                + "## Your Certificate\n"
                + "Subject Name: " + name + "\n"
                + "Unique ID: " + id + "\n"
                + "Issuer Name: " + issuer + "\n\n"
                + "=> / â† Home\n\n"
                + "---\n"
                + "ðŸ“‚ Source: context/CertificateController.java â€” demos @Context with X509Certificate and URI.\n");
    }
}
