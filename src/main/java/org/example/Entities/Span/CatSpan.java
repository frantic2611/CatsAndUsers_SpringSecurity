package org.example.Entities.Span;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CatSpan {
    private String name;
    private String breed;
    private String color;
    private Integer ownerId;
    private LocalDate birth;
    private List<Integer> friends;
}
