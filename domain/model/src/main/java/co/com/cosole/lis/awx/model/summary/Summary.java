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
public class Summary {
    private List<HostStatus> successfulHosts = new ArrayList<>();
    private List<HostStatus> failedHosts = new ArrayList<>();

}
