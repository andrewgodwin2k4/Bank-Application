package com.andrew.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

public class TransactionServlet extends HttpServlet {

    private TransactionService service = new TransactionService();
    private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {

            long accountNumber = Long.parseLong(req.getParameter("accountNumber"));
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

                TransferRequest request = mapper.readValue(req.getInputStream(), TransferRequest.class);

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

                DepositRequest request = mapper.readValue(req.getInputStream(), DepositRequest.class);

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