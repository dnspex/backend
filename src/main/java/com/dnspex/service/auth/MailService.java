package com.dnspex.service.auth;

import com.dnspex.util.rest.exception.HttpResponse;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@ApplicationScoped
public class MailService {

    @ConfigProperty(name = "resend.enabled")
    Boolean enabled;

    @ConfigProperty(name = "resend.api-key")
    String apiKey;

    @ConfigProperty(name = "resend.domain")
    String domain;

    private static final Logger LOG = Logger.getLogger(MailService.class);

    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)",
            Pattern.CASE_INSENSITIVE
    );


    public final Resend resend = new Resend(this.apiKey);

    public void send(String from, String to, String templateId, Template.Variable... variables) { //ToDo: async threads with Uni<>
        CreateEmailOptions params = CreateEmailOptions.builder().from("DNSPEX <" + from + "@"+domain+">").to(to)
                .template(Template.builder().id(templateId).variables(variables).build()).build();

        try {
            if(!enabled) {
                this.sendDev(params);
                return;
            }

            resend.emails().send(params);
        } catch (ResendException e) { //ToDo: handle resend exceptions and log them, also log sent emails for better support and debugging
            System.out.println("Error sending email: " + e.getMessage());
            throw new HttpResponse(Response.Status.INTERNAL_SERVER_ERROR, "EMAIL_SENDING_FAILED");
        }
    }

    private void sendDev(CreateEmailOptions emailOptions) {
        List<String> links = this.links(emailOptions.getText());

        LOG.info("===============================================================");
        LOG.infof("TO: %s", String.join(", ", emailOptions.getTo()));
        LOG.infof("SUBJECT: %s", emailOptions.getSubject());

        if (links.isEmpty()) LOG.info("LINKS: none");
        else {
            LOG.info("LINKS:");
            for (String link : links) {
                LOG.info(link);
            }
        }

        LOG.info("===============================================================\n");
    }

    private List<String> links(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();

        Matcher matcher = URL_PATTERN.matcher(text);
        Set<String> links = new LinkedHashSet<>();

        while (matcher.find()) links.add(matcher.group(1));

        return new ArrayList<>(links);
    }

    public boolean isReachable() {
        try {
            return this.resend.domains().list() != null;
        } catch (ResendException e) {
            return false;
        }
    }
}