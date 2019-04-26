package am.arssystems.image_manager_server.security;


import am.arssystems.image_manager_server.model.User;
import am.arssystems.image_manager_server.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Qualifier("currentUserDetailServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.setHeader("Access-Control-Allow-Origin","*");
//        response.setHeader("Access-Control-Allow-Methods","*");
//        final String requestHeader = request.getHeader(this.tokenHeader);
//        String currentUserUsername = null;
//        String authToken = null;
//        User currentUser = null;
//        if (requestHeader != null && requestHeader.startsWith("*LilMag* ")) {
//            authToken = requestHeader.substring(9);
//            try {
//                 jwtTokenUtil.getUsernameFromToken(authToken);
//            }catch (ExpiredJwtException e){
//                response.sendError(401,"Unauthorized");
//                return;
//            }
//            int id = (int) jwtTokenUtil.getAllClaimsFromToken(authToken).get("id");
//            currentUser = userRepository.findAllById(id);
//            if (currentUser == null) {//երբ տվյալ օգտատերը արդեն ամբողջությամբ ջնջվել է մեր համակարգից՝ ջնջվել է ՏԲ-ից։
////                response.setStatus(102, "Դուք ջնջվել եք մեր համակարգից, խնդրում ենք նորից գրանցվել");
//                response.sendError(410,"Դուք ջնջվել եք մեր համակարգից, խնդրում ենք նորից գրանցվել");
//                return;
//            } else {
//                try {
//                    currentUserUsername = jwtTokenUtil.getUsernameFromToken(authToken);
//                    String tokenPass = (String) jwtTokenUtil.getAllClaimsFromToken(authToken).get("password");
//                    if (!currentUserUsername.equals(currentUser.getUsername().trim()) || !currentUser.getPassword().equals(tokenPass)) {
//                        response.sendError(403, "Փոփոխվել է էլ հասցե կամ գաղտնաբառ");
//                        return;
//                    }
//                } catch (ExpiredJwtException e) {
////                // սա աշխատում է այն ժամանակ երբ թոկենի ժամանակը լրացել է, այսինքն թոկենը էլ ակտիվ չէ
//                } catch (Exception e) {
//                    logger.error(e);
//                }
//            }
//        } else {
////            logger.warn("couldn't find *LilMag* string, will ignore the header");
//        }
//        if (currentUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            String usernamelowercase = currentUser.getUsername().toLowerCase();
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(usernamelowercase);
//            int tokenId = (int) jwtTokenUtil.getAllClaimsFromToken(authToken).get("id");
//            if (currentUserUsername != null && jwtTokenUtil.validateToken(authToken, userDetails.getUsername().trim()) &&
//                    userDetails.getPassword().equals(jwtTokenUtil.getAllClaimsFromToken(authToken).get("password")) &&
//                    currentUser.getId() == tokenId && currentUser.getUsername().trim().equals(currentUserUsername)) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
        chain.doFilter(request, response);
    }
}

