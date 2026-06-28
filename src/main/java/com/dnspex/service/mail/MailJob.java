package com.dnspex.service.mail;

import com.resend.services.emails.model.Template;

public record MailJob(String from, String to, String templateId, Template.Variable[] variables) {}
