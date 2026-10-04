package com.agenticIde.distributed_lovable.account_service.mapper;
import com.agenticIde.distributed_lovable.account_service.dto.auth.SignupRequest;
import com.agenticIde.distributed_lovable.account_service.dto.auth.UserProfileResponse;
import com.agenticIde.distributed_lovable.account_service.entity.User;
import com.agenticIde.distributed_lovable.comman_lib.dto.UserDto;
import com.agenticIde.distributed_lovable.comman_lib.security.JwtUserPrinciple;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupRequest signupRequest);
    @Mapping(source = "userId", target = "id")
    UserProfileResponse toUserProfileResponse(JwtUserPrinciple user);
    UserDto toUserDto(User user);
}
