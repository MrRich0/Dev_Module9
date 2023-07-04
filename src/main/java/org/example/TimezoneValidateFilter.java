package org.example;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {

        String timeZone = request.getParameter("timezone");

        if (timeZone == null) {
            timeZone = "UTC";
        }

        if (isValidTimezone(timeZone)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(404);
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("<h1>Invalid timezone</h1>");
            response.getWriter().close();

        }
    }

    private boolean isValidTimezone(String timeZone) {
        try {
            ZoneId.of(timeZone.replace(" ", "+"));
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
