package com.andrew.transaction;

import com.andrew.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

public class TransactionServlet extends HttpServlet {

    private TransactionService service = new TransactionService();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            int accountId = Integer.parseInt(req.getParameter("accountId"));
            List<Transaction> transactions = service.getTransactions(accountId);

            res.setContentType("application/json");
            mapper.writeValue(res.getOutputStream(), transactions);

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}