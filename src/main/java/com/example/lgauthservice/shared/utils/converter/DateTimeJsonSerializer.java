package com.example.lgauthservice.shared.utils.converter;

import com.example.lgauthservice.shared.constants.Constants;
import com.example.lgauthservice.shared.utils.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DateTimeJsonSerializer extends JsonSerializer<Instant> {

    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_YYYYMMDD_HYPHEN).withZone(DateUtils.SYSTEM_DEFAULT_ZONE);

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeString(Strings.EMPTY);
            return;
        }
        String str = fmt.format(value);
        gen.writeString(str);
    }
}