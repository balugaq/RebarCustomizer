package com.balugaq.rc.config.pack;

import com.balugaq.rc.config.ConfigReader;
import com.balugaq.rc.config.Deserializer;
import com.balugaq.rc.config.Examinable;
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
public class WebsiteLink implements Deserializer<WebsiteLink>, Examinable<WebsiteLink> {
    private final String link;

    @Override
    public WebsiteLink examine() throws ExamineFailedException {
        if (!link.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
            throw new ExamineFailedException("WebsiteLink must be ^(https?|ftp)://[^\\s/$.?#].[^\\s]*$");
        }
        return this;
    }

    @Override
    public List<ConfigReader<?, WebsiteLink>> readers() {
        return ConfigReader.list(String.class, WebsiteLink::new);
    }
}
