package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.PostReactDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.PostReact;
import com.example.socialnetwork.model.User;
import org.springframework.stereotype.Component;

@Component
public class PostReactConverter {
    public PostReact toEntity(PostReactDto postReactDto) {
        PostReact postReact = new PostReact();

        User user = new User();
        user.setId(postReactDto.getUser().getId());
        postReact.setUser(user);

        return postReact;
    }

    public PostReactDto toDto(PostReact postReact) {
        PostReactDto postReactDto = new PostReactDto();
        postReactDto.setId(postReact.getId());

        UserDto userDto = new UserDto();
        userDto.setId(postReact.getUser().getId());
        userDto.setUsername(postReact.getUser().getUsername());
        userDto.setAvatar(postReact.getUser().getAvatar());
        postReactDto.setUser(userDto);

        return postReactDto;
    }
}
