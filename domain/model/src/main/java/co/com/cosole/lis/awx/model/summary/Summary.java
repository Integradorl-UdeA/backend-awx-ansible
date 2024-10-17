package co.com.cosole.lis.awx.model.summary;
import co.com.cosole.lis.awx.model.hoststatus.HostStatus;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Summary {
    private List<HostStatus> successfulHosts;
    private List<HostStatus> failedHosts;
}
