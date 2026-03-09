package com.andrew.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import com.andrew.user.UserDAO;

public class TransactionServlet extends HttpServlet {

    private TransactionService service = new TransactionService();
    private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {

            Integer userId = (Integer) req.getSession().getAttribute("userId");

            if(userId == null) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().println("User not logged in");
                return;
            }

            long accountNumber = Long.parseLong(req.getParameter("accountNumber"));

            UserDAO userDAO = new UserDAO();

            if(!userDAO.userOwnsAccount(userId, accountNumber)) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().println("You do not own this account");
                return;
            }

            List<Transaction> transactions = service.getTransactions(accountNumber);

            res.setContentType("application/json");
            mapper.writeValue(res.getOutputStream(), transactions);

        } catch (Exception e) {

            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {

            if(req.getRequestURI().endsWith("/transfer")) {

                Integer userId = (Integer) req.getSession().getAttribute("userId");

                if(userId == null) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().println("User not logged in");
                    return;
                }

                TransferRequest request = mapper.readValue(req.getInputStream(), TransferRequest.class);

                UserDAO userDAO = new UserDAO();

                if(!userDAO.userOwnsAccount(userId, request.getFromAccount())) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().println("You do not own this account");
                    return;
                }

                service.transfer(
                        request.getFromAccount(),
                        request.getToAccount(),
                        request.getAmount()
                );

                res.getWriter().println("{\"status\":\"TRANSFER SUCCESS\"}");
            }

            else if(req.getRequestURI().endsWith("/deposit")) {

                DepositRequest request = mapper.readValue(req.getInputStream(), DepositRequest.class);

                service.deposit(
                        request.getAccountNumber(),
                        request.getAmount()
                );

                res.getWriter().println("{\"status\":\"DEPOSIT SUCCESS\"}");
            }

            else if(req.getRequestURI().endsWith("/withdraw")) {

                Integer userId = (Integer) req.getSession().getAttribute("userId");

                if(userId == null) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().println("User not logged in");
                    return;
                }

                DepositRequest request = mapper.readValue(req.getInputStream(), DepositRequest.class);

                UserDAO userDAO = new UserDAO();

                if(!userDAO.userOwnsAccount(userId, request.getAccountNumber())) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().println("You do not own this account");
                    return;
                }

                service.withdraw(
                        request.getAccountNumber(),
                        request.getAmount()
                );

                res.getWriter().println("{\"status\":\"WITHDRAW SUCCESS\"}");
            }

        } catch (Exception e) {

            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println(e.getMessage());

        }
    }
}