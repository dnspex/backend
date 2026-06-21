package com.dnspex.service.auth;

import com.dnspex.dto.request.user.auth.AuthLoginRequest;
import com.dnspex.dto.request.user.auth.AuthRegisterRequest;
import com.dnspex.dto.request.user.auth.AuthResetRequest;
import com.dnspex.entity.auth.Session;
import com.dnspex.entity.user.PendingAction;
import com.dnspex.entity.user.User;
import com.dnspex.service.user.PendingActionService;
import com.dnspex.service.user.SessionService;
import com.dnspex.service.user.UserService;
import com.dnspex.util.enumeration.PendingActionType;
import com.dnspex.util.enumeration.UserState;
import com.dnspex.util.rest.exception.HttpResponse;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AuthService {
    @Inject
    UserService userService;

    @Inject
    SessionService sessionService;

    @Inject
    PendingActionService pendingActionService;

    @Transactional
    public Map<String, String> login(AuthLoginRequest request) {
        User user = this.userService.findByEmail(request.email());

        if (!BcryptUtil.matches(request.password(), user.getPassword())) throw new HttpResponse(Response.Status.BAD_REQUEST, "INVALID_CREDENTIALS");

        UserState state = user.getState();
        if (state.equals(UserState.INACTIVE)) throw new HttpResponse(Response.Status.BAD_REQUEST, "ACCOUNT_INACTIVE");
        if (state.equals(UserState.DELETED)) throw new HttpResponse(Response.Status.BAD_REQUEST, "ACCOUNT_DELETED");

        user.setLastLoginAt(LocalDateTime.now());
        user.persist();

        return this.sessionService.create(user);
    }

    @Transactional
    public void register(AuthRegisterRequest request) {
        if (this.userService.existsByEmail(request.email())) throw new HttpResponse(Response.Status.BAD_REQUEST, "EMAIL_ALREADY_REGISTERED");

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(BcryptUtil.bcryptHash(request.password()));
        user.persist();

        PendingAction action = this.pendingActionService.create(user, PendingActionType.VERIFY_EMAIL);
        action.persist(); //ToDo: send verification email
    }

    @Transactional
    public void logout(String refreshToken) {
        Session session = this.sessionService.findByRefreshToken(refreshToken);
        if (session.isExpired()) throw new HttpResponse(Response.Status.BAD_REQUEST, "REFRESH_TOKEN_EXPIRED");

        User user = session.getUser();
        List<Session> sessions = user.getSessions();

        sessions.remove(session);
        session.delete();

        user.persist();
    }

    @Transactional
    public void verify(String token) {
        PendingAction action = this.pendingActionService.get(token);

        PendingActionType type = action.getType();
        if (!type.equals(PendingActionType.VERIFY_EMAIL)) throw new HttpResponse(Response.Status.BAD_REQUEST, "INVALID_ACTION_TYPE");

        User user = action.getUser();
        user.setState(UserState.ACTIVE);
        user.activate();
        user.persist();

        action.setUsedAt(LocalDateTime.now());
        action.persist(); // ToDo: send welcome email maybe
    }

    @Transactional
    public void requestPasswordReset(String email) {
        User user = this.userService.findByEmail(email);

        PendingAction action = this.pendingActionService.create(user, PendingActionType.PASSWORD_RESET);
        action.persist(); //ToDO: send password reset email
    }

    @Transactional
    public void resetPassword(AuthResetRequest request) { // add geolocation and timestamp
        PendingAction action = this.pendingActionService.get(request.refreshToken());

        PendingActionType type = action.getType();
        if (!type.equals(PendingActionType.PASSWORD_RESET)) throw new HttpResponse(Response.Status.BAD_REQUEST, "INVALID_ACTION_TYPE");

        String password = request.password();

        User user = action.getUser();
        user.setPassword(BcryptUtil.bcryptHash(password));
        user.persist();

        action.setUsedAt(LocalDateTime.now());
        action.persist(); //ToDO: send password reset information mail
    }
}
