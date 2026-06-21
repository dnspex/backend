package com.dnspex.service.user;

import com.dnspex.entity.user.PendingAction;
import com.dnspex.entity.user.User;
import com.dnspex.repository.PendingActionRepository;
import com.dnspex.util.enumeration.PendingActionType;
import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PendingActionService {

    @Inject
    PendingActionRepository pendingActionRepository;

    @Transactional
    public PendingAction create(User user, PendingActionType type) {
        PendingAction action = new PendingAction();
        action.setType(type);
        action.setExpiresAt(type.getExpiresAt());
        action.setUser(user);

        return action;
    }

    public PendingAction get(String token) {
        PendingAction pendingAction = this.findByToken(token);
        if(pendingAction.isExpired()) throw new HttpResponse(Response.Status.BAD_REQUEST, "TOKEN_EXPIRED");

        if (pendingAction.getUsedAt() != null) throw new HttpResponse(Response.Status.BAD_REQUEST, "TOKEN_ALREADY_USED");

        return pendingAction;
    }

    private PendingAction findByToken(String token) {
        return pendingActionRepository.findByToken(token)
                .orElseThrow(() -> new HttpResponse(
                        Response.Status.BAD_REQUEST, "INVALID_ACTION_TOKEN"
                ));
    }
}
