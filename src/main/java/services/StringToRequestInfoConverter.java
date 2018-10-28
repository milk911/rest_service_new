package services;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import javax.servlet.ServletException;

public class StringToRequestInfoConverter implements Converter<String, RequestInfo> {
    @Nullable
    @Override
    public RequestInfo convert(String s){
        int id = Integer.parseInt(s);
        if (ScriptActions.getHistory().containsKey(id)) {
            return ScriptActions.getHistory().get(id);
        }
        else {
            return null;
        }
    }
}
