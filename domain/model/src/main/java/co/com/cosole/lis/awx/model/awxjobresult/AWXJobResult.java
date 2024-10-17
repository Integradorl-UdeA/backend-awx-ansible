package co.com.cosole.lis.awx.model.awxjobresult;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AWXJobResult {

    private Integer jobId;
    private String type;
    private String url;
}
