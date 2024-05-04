package org.example.Entities.Span;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserSpan {
    private String username;
    private String password;
    private String role;
    private String name;
    private LocalDate birth;
}
