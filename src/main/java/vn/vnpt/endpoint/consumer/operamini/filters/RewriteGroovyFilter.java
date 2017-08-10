package vn.vnpt.endpoint.consumer.operamini.filters;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Timer;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 6/30/2017.
 */
@WebFilter(filterName="rewriteGroovyFilter")
public class RewriteGroovyFilter implements Filter{


    FilterConfig config;
    public void setFilterConfig(FilterConfig config) {
        this.config = config;
    }

    public FilterConfig getFilterConfig() {
        return config;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        setFilterConfig(config);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MetricRegistry mrDef = SharedMetricRegistries.getOrCreate("default");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //String url = httpServletRequest.getRequestURI();
        //String contextPath = httpServletRequest.getContextPath();
        String servletPath = httpServletRequest.getServletPath();
        //ServletContext context = getFilterConfig().getServletContext();
        //System.out.println("groovy servletPath = "+servletPath);
        if(servletPath.toLowerCase().contains(".groovy")){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            //test on http://www.regexe.com/
            String neServletPath = servletPath.replaceFirst("([/\\\\])g(([/\\\\][a-zA-Z0-9]+)*)([/\\\\]?[^/?\\\\#]+)","$1g$2$4.groovy");
            //((HttpServletResponse) servletResponse).sendRedirect(newUrl);
            String timeRegex = "([/\\\\])g(([/\\\\][a-zA-Z0-9]+)*)([/\\\\]?[^/?\\\\#]+)";
            Pattern pattern = Pattern.compile(timeRegex);
            Matcher matcher = pattern.matcher(servletPath);
            String key = "";
            if (matcher.matches()) {
                String s1 = matcher.group(1);
                String s2 = matcher.group(2);
                String s4 = matcher.group(4);
                key = s1+"g"+s2+s4+".groovy";
            }
            //mrDef.meter("meter:"+key).mark();
            Timer.Context timer = mrDef.timer("timer:"+key).time();
            servletRequest.getRequestDispatcher(neServletPath).forward(servletRequest, servletResponse);
            timer.close();
        }
    }

    @Override
    public void destroy() {

    }
}
