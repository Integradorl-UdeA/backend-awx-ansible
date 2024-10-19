package co.com.cosole.lis.awx.model.inventories;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class GroupsInventories {

    private Integer count;
    private List<Group> results;
}
