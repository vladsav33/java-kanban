package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalTime localTime) throws IOException {
        if (localTime != null) {
            jsonWriter.value(localTime.format(formatterWriter));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalTime read(final JsonReader jsonReader) throws IOException {
        return LocalTime.parse(jsonReader.nextString(), formatterWriter);
    }
}
