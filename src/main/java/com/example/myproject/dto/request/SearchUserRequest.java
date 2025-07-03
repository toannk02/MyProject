package com.example.myproject.dto.request;

import com.example.myproject.dto.SearchRequestDTO;
import com.example.myproject.entity.User;
import com.example.myproject.util.Constants;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequest extends SearchRequestDTO {
    private String keyWord;
    private String username;
    private Boolean active;


    public void validateInput(){
        super.validateInput();
        if(keyWord != null)
            keyWord = keyWord.trim().toLowerCase();
        if(username != null)
            username = username.trim().toLowerCase();
        //sortBy = Constants.validateSort(sortBy, Constants.DefaultValuePage.SORT_BY, User.class.getDeclaredFields());
    }
}
