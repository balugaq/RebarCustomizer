package com.balugaq.rc.config.pack;

import com.balugaq.rc.config.ConfigReader;
import com.balugaq.rc.config.Deserializer;
import com.balugaq.rc.config.Examinable;
import com.balugaq.rc.config.PackDesc;
import com.balugaq.rc.exceptions.ExamineFailedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @author balugaq
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@NullMarked
public class PackID implements Deserializer<PackID>, Examinable<PackID> {
    private final String id;

    @Override
    public PackID examine() throws ExamineFailedException {
        if (!id.matches("[A-Za-z0-9_\\+\\-]+")) {
            throw new ExamineFailedException("Pack ID must be [A-Za-z0-9_+-]+");
        }
        return this;
    }

    @Override
    public List<ConfigReader<?, PackID>> readers() {
        return ConfigReader.list(String.class, PackID::new);
    }

    public PackDesc toDesc() {
        return new PackDesc(id);
    }
}
