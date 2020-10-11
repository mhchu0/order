package equipmgnt;

import equipmgnt.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverApprovalObtained_Updatestatus(@Payload ApprovalObtained approvalObtained){

        if(approvalObtained.isMe()){
            System.out.println("##### listener Updatestatus : " + approvalObtained.toJson());

            Optional<Order> orderOptional = orderRepository.findById(approvalObtained.getOrderId());
            Order order = orderOptional.get();
            order.setStatus(approvalObtained.getStatus());

            System.out.println("##### payment id : " + approvalObtained.getId());

            orderRepository.save(order);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCancelRequested_Updatestatus(@Payload CancelRequested cancelRequested){

        if(cancelRequested.isMe()){
            System.out.println("##### listener Updatestatus : " + cancelRequested.toJson());
            if(cancelRequested.getStatus().equals("CANCELED")) {
                Optional<Order> orderOptional = orderRepository.findById(cancelRequested.getOrderId());
                Order order = orderOptional.get();
                order.setStatus(cancelRequested.getStatus());

                orderRepository.save(order);
            }

        }
    }


}
