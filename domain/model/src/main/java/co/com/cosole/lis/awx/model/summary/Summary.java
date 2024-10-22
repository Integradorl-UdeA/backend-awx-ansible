package co.com.cosole.lis.awx.model.summary;
import co.com.cosole.lis.awx.model.hoststatus.HostStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Summary {
    private List<String> successfulHosts = new ArrayList<>();
    private List<String> failedHosts = new ArrayList<>();

}
