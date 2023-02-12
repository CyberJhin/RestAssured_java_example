package org.example.pojo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessCreateOrderResponse {
    private String name;
    private GetOrderResponse order;
    private boolean success;
}
