package co.com.cosole.lis.awx.model.awxjobresult;

import co.com.cosole.lis.awx.model.hoststatus.HostsStatus;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class JobCompletationNotification {
    private String id;
    private String name;
    private String status;
    private Map<String, HostsStatus> hosts = new HashMap<>();
}
