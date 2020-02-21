package com.hxy.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

@Slf4j
public class UserSerialization {

    private static final String TARGET_PATH = "avro.store";

    public static void main(String[] args) throws IOException {
        new UserSerialization().serialize();
        new UserSerialization().deserialize();
    }

    private void serialize() throws IOException {
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);

        User user1 = new User("hxy", 16, "123");
        User user2 = new User("abc", 20, "321");

        Schema schema = User.getClassSchema();
        log.debug("schema is: {}", schema);

        dataFileWriter.create(schema, new File(TARGET_PATH));
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);

        dataFileWriter.close();
    }

    private void deserialize() throws IOException {
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<>(new File(TARGET_PATH), userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }

    }

}
