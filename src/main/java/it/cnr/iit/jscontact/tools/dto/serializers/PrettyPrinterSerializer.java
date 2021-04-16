package it.cnr.iit.jscontact.tools.dto.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrettyPrinterSerializer {

    public static String print(Object o) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);

    }
}
