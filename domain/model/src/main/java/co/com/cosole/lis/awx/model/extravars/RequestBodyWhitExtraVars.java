package co.com.cosole.lis.awx.model.extravars;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class RequestBodyWhitExtraVars {

    private ExtraVars extra_vars;
}
