package io.github.rahulrajsonu.securexai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginAttemptsLogger {

    @EventListener
    public void auditEventHappened(
            AuditApplicationEvent auditApplicationEvent) {
        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();

        WebAuthenticationDetails details
                = (WebAuthenticationDetails) auditEvent.getData().get("details");

        AuditEventLog auditEventLog = new AuditEventLog(
                auditEvent.getPrincipal() + " " + auditEvent.getType(),
                details.getRemoteAddress(),
                details.getSessionId(),
                auditEvent.getData().get("requestUrl"));
        log.info("{}",auditEventLog);
    }

    private static record AuditEventLog(String principal, String remoteIpAddress, String sessionId, Object url){}
}

