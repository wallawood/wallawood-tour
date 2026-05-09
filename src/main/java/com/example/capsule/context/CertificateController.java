package com.example.capsule.context;

import io.gemboot.CertUtil;
import io.gemboot.annotations.Path;
import io.gemboot.annotations.Context;
import io.gemboot.GeminiResponse;
import io.gemboot.annotations.GeminiController;
import io.gemboot.annotations.RequireCertificate;

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
                + "=> / ← Home\n\n"
                + "---\n"
                + "📂 Source: context/CertificateController.java — demos @Context with X509Certificate and URI.\n");
    }
}
