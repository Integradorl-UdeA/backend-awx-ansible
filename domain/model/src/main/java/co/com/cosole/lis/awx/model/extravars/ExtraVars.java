package co.com.cosole.lis.awx.model.extravars;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class ExtraVars {
    private String hosts_name;
}
