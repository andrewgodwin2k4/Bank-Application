package com.andrew.account;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;

public class AccountServlet extends HttpServlet {

    private ObjectMapper mapper = new ObjectMapper();
    private AccountService service = new AccountService();

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
        try {
            AccountRequest request = mapper.readValue(req.getInputStream(), AccountRequest.class);

            Account account = service.createAccount(request.getName(), request.getInitialDeposit());

            res.setStatus(HttpServletResponse.SC_CREATED);
            res.setContentType("application/json");

            mapper.writeValue(res.getOutputStream(), account);

        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Account account = service.getAccount(id);

            if (account == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().println("Account not found");
                return;
            }

            res.setContentType("application/json");

            mapper.writeValue(res.getOutputStream(), account);

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}