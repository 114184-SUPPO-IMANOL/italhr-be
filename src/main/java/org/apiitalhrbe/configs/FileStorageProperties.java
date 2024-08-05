package org.apiitalhrbe.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileStorageProperties {

    private String uploadDir;
}
