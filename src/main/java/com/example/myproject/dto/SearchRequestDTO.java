package com.example.myproject.dto;

import com.example.myproject.util.Constants;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SearchRequestDTO {
    private Integer pageIndex;
    private Integer pageSize;
    protected String orderBy;

    public void validateInput(){
        if(pageIndex == null || pageIndex < 1)
            pageIndex = Constants.DefaultValuePage.PAGE_INDEX;

        if(pageSize == null || pageSize <= 0)
            pageSize = Constants.DefaultValuePage.PAGE_SIZE;

        orderBy = Constants.validateOrder(orderBy);
    }
}
