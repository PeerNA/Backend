package cos.peerna.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // Code to perform after a failed authentication

        // Set the response status code to 401 (Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", exception.getMessage());
        responseMap.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        // Convert the map to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(responseMap);

        // Set the response content type to JSON
        response.setContentType("application/json");

        // Write the JSON string in the response
        response.getWriter().write(responseJson);

//        response.sendRedirect("http://localhost:3000:3000/callback?login=fail");
        response.sendRedirect("https://www.peerna.kr?login=fail");
    }
}
