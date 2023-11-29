    package com.example.pepelingbackendv2.configs;

    import com.example.pepelingbackendv2.Responses.ErrorResponse;
    import com.example.pepelingbackendv2.Responses.RegistrationResponse;
    import com.google.gson.Gson;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.stereotype.Component;

    import java.io.IOException;
    import java.io.PrintWriter;
    import java.io.Serializable;
    @Component
    public class UnauthorizedEndpoint implements AuthenticationEntryPoint, Serializable {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            RegistrationResponse response1 = new RegistrationResponse();
            response1.setError(true);
            response1.setMessage("loginPage.errors.wrongData");
            Gson gson =new Gson();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.write(gson.toJson(response1));
            out.flush();
            out.close();
        }
    }
