package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.CommentReactDto;
import com.example.socialnetwork.dto.PostReactDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.CommentReact;
import com.example.socialnetwork.model.PostReact;
import com.example.socialnetwork.model.User;
import org.springframework.stereotype.Component;

@Component
public class CommentReactConverter {
    public CommentReact toEntity(CommentReactDto commentReactDto) {
        CommentReact commentReact = new CommentReact();

        User user = new User();
        user.setId(commentReactDto.getUser().getId());
        commentReact.setUser(user);

        return commentReact;
    }

    public CommentReactDto toDto(CommentReact commentReact) {
        CommentReactDto commentReactDto = new CommentReactDto();
        commentReactDto.setId(commentReact.getId());

        UserDto userDto = new UserDto();
        userDto.setId(commentReact.getUser().getId());
        userDto.setUsername(commentReact.getUser().getUsername());
        userDto.setAvatar(commentReact.getUser().getAvatar());
        commentReactDto.setUser(userDto);

        return commentReactDto;
    }
}
