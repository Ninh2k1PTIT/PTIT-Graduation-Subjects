package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.TagDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.Tag;
import com.example.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TagConverter {
    private UserConverter userConverter;
    public Tag toEntity(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setOffsetTop(tagDto.getOffsetTop());
        tag.setOffsetLeft(tagDto.getOffsetLeft());
        tag.setUser(userConverter.toEntity(tagDto.getUser()));

        return tag;
    }

    public TagDto toDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setPostPhotoId(tag.getPostPhoto().getId());
        tagDto.setOffsetLeft(tag.getOffsetLeft());
        tagDto.setOffsetTop(tag.getOffsetTop());
        tagDto.setUser(userConverter.toDto(tag.getUser()));

        return tagDto;
    }
}
