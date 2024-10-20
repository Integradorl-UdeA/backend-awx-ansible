package co.com.cosole.lis.awx.model.awxjobresult;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class AWXJobResult {

    private Integer job;
    private String type;
    private String url;
}
