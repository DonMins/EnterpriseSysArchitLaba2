package com.ex.task;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is for receiving JSON data.
 *
 * @author Zdornov Maxim
 * @version 1.0
 *
 */

@Data
@NoArgsConstructor
public class YouNumberOfjson {
    private String youNumber;
    private String properties;
}
