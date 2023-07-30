package cos.peerna.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Code to perform after a successful authentication
        HttpSession session = request.getSession();

        // Create a map with the key-value pair "message":"success"
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("message", "success");

        // Convert the map to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(responseMap);

        // Set the response content type to JSON
        response.setContentType("application/json");

        // Write the JSON string in the response
        response.getWriter().write(responseJson);

//        response.sendRedirect("http://localhost:3000/callback?login=success");
        response.sendRedirect("https://peerna.kr/callback?login=success");
    }
}
