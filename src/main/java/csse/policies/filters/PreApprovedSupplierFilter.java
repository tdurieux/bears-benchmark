package csse.policies.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import csse.items.Item;
import csse.common.ResponseWrapper;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * *** Example filter implementation ***
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 10/5/2018.
 */
public class PreApprovedSupplierFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, responseWrapper);

        if(HttpMethod.GET.matches(httpRequest.getMethod())) {

            String responseContent = new String(responseWrapper.getDataStream());
            List<Item> itemList = getResponseItems(responseContent);

            itemList.add(new Item("TEST ITEM", null, "TestCategory", 200.00,"desc"));
            byte[] responseToSend = createByteArray(itemList);
            servletResponse.getOutputStream().write(responseToSend);

        }

    }

    @Override
    public void destroy() {

    }

    private List<Item> getResponseItems(String responseContent) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Item[] items = objectMapper.readValue(responseContent, Item[].class);
        List<Item> itemList = new ArrayList<>();

        itemList.addAll(Arrays.asList(items));

        return itemList;
    }

    private byte[] createByteArray(Object response) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(response);
        return serialized.getBytes();
    }

}
