package com.cqrs.socialfeed.api.user;

import com.cqrs.socialfeed.api.file.FileUploader;
import com.cqrs.socialfeed.api.security.CustomUserDetails;
import com.cqrs.socialfeed.command.user.command.UpdateProfileImageCommand;
import com.cqrs.socialfeed.command.user.usecase.UpdateProfileImageUseCase;
import com.cqrs.socialfeed.query.follow.response.FollowStatsResponse;
import com.cqrs.socialfeed.query.follow.usecase.FollowQueryUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final FollowQueryUseCase followQueryUseCase;
    private final FileUploader fileUploader;
    private final UpdateProfileImageUseCase updateProfileImageUseCase;

    public UserController(FollowQueryUseCase followQueryUseCase, FileUploader fileUploader, UpdateProfileImageUseCase updateProfileImageUseCase) {
        this.followQueryUseCase = followQueryUseCase;
        this.fileUploader = fileUploader;
        this.updateProfileImageUseCase = updateProfileImageUseCase;
    }

    @GetMapping("/me")
    public ResponseEntity<GetMyProfileResponse> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        GetMyProfileResponse response = new GetMyProfileResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getProfileImageUrl()
        );

        return ResponseEntity.ok(response);
    }


    // TODO 테스트 필요 - 용량 제한 및 업로드 경로 지정
    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfileImage(
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        String imageUrl = fileUploader.upload(file);

        UpdateProfileImageCommand command = new UpdateProfileImageCommand(userDetails.getId(), imageUrl);
        updateProfileImageUseCase.updateProfileImage(command);

        return ResponseEntity.ok().build();
    }

    // 팔로워 수: 나를 팔로우한 사람 수
    // 팔로잉 수: 내가 팔로우한 사람 수
    @GetMapping("/follow-stats")
    public ResponseEntity<GetMyFollowStatResponse> getFollowStats( @AuthenticationPrincipal CustomUserDetails userDetails) {
        FollowStatsResponse followStatsResponse = followQueryUseCase.getStats(userDetails.getId());

        return ResponseEntity.ok(new GetMyFollowStatResponse(followStatsResponse.getFollowerCount(), followStatsResponse.getFollowingCount()));
    }
}
