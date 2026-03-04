package com.andrew.transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;

import java.io.IOException;

public class TransferServlet extends HttpServlet {

    private ObjectMapper mapper = new ObjectMapper();
    private TransferService service = new TransferService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            TransferRequest request = mapper.readValue(req.getInputStream(), TransferRequest.class);

            service.transfer(
                    request.getFromAccount(),
                    request.getToAccount(),
                    request.getAmount()
            );

            res.setContentType("application/json");
            res.getWriter().println("{\"status\":\"SUCCESS\"}");

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println(e.getMessage());
        }
    }
}