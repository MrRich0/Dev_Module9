package org.example;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/time")

public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;
    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(getServletContext().getRealPath("templates/"));
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        String timeZone = request.getParameter("timezone");

        if (timeZone != null ) {
            response.addCookie(new Cookie("utc",timeZone.replace(" ","+")));
        }else {
                Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    timeZone = cookie.getValue();
                }
            }else{
                response.addCookie(new Cookie("utc", "UTC"));
                timeZone = "UTC";
            }
        }

        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(timeZone.replace(" ","+")));

        String result = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Context context = new Context(
                request.getLocale(),
                Map.of("time",result,
                        "utc",timeZone.replace(" ","+"))
                );
        engine.process("time", context, response.getWriter());

        response.getWriter().close();

    }
}

