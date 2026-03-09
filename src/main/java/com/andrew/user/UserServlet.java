package com.andrew.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;

import java.io.IOException;

public class UserServlet extends HttpServlet {

    private UserService service = new UserService();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {

            if(req.getRequestURI().endsWith("/register")) {

                RegisterRequest request = mapper.readValue(req.getInputStream(), RegisterRequest.class);

                User user = service.register(request.getUsername(), request.getPassword());

                res.setContentType("application/json");
                mapper.writeValue(res.getOutputStream(), user);
            }

            else if(req.getRequestURI().endsWith("/login")) {

                LoginRequest request = mapper.readValue(req.getInputStream(), LoginRequest.class);

                User user = service.login( request.getUsername(), request.getPassword());

                HttpSession session = req.getSession();
                session.setAttribute("userId", user.getUserId());

                res.getWriter().println("{\"status\":\"LOGIN SUCCESS\"}");
            }

            else if(req.getRequestURI().endsWith("/link-account")) {

                Integer userId = (Integer) req.getSession().getAttribute("userId");

                if(userId == null) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().println("User not logged in");
                    return;
                }

                LinkAccountRequest request = mapper.readValue(req.getInputStream(), LinkAccountRequest.class);
                service.linkAccount(userId, request.getAccountNumber());
                res.getWriter().println("{\"status\":\"ACCOUNT LINKED\"}");
            }

        } catch(Exception e) {

            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println(e.getMessage());

        }
    }
}