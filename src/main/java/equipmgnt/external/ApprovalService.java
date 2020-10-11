
package equipmgnt.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="approval", url="${api.approval.url}")
public interface ApprovalService {

    @RequestMapping(method= RequestMethod.POST, path="/approvals")
    public void requestapprove(@RequestBody Approval approval);

}