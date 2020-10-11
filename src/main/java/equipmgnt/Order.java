package equipmgnt;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long equipmentId;
    private String status;
    private Integer qty;

    @PostUpdate
    public void onPostUpdate(){
        if(this.getStatus().equals("CANCELLED")) {
            OrderCanceled orderCanceled = new OrderCanceled();
            BeanUtils.copyProperties(this, orderCanceled);
            orderCanceled.publishAfterCommit();
        }

    }

    @PostPersist
    public void onPostPersist(){

        try {
            Thread.currentThread().sleep((long) (800 + Math.random() * 220));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.


        equipmgnt.external.Approval approval = new equipmgnt.external.Approval();

        approval.setOrderId(ordered.getId());
        approval.setQty(ordered.getQty());
        approval.setEquipmentId(ordered.getEquipmentId());
        System.out.println("##### 오더아이디 어디감 : " + ordered.getId());
        approval.setStatus("APPROVED");
        // mappings goes here
        OrderApplication.applicationContext.getBean(equipmgnt.external.ApprovalService.class)
            .requestapprove(approval);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }




}
