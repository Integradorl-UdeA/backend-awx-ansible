package co.com.cosole.lis.awx.model.inventories;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Group {
    private Integer id;
    private String name;
}
