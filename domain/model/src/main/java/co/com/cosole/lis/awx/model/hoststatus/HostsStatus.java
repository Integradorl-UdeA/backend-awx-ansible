package co.com.cosole.lis.awx.model.hoststatus;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class HostsStatus {
    private boolean failed;
}
