package com.portfolio.portfolio.hls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PathRequest {
    private String name;
    private Integer timeout;
}
