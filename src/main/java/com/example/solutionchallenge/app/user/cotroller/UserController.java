package com.example.solutionchallenge.app.user.cotroller;

import com.example.solutionchallenge.app.auth.domain.constant.ErrorCode;
import com.example.solutionchallenge.app.common.dto.response.ResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseUtil;
import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.dto.UserResponseDto;
import com.example.solutionchallenge.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저", description = "유저 API")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "API 정상 작동")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "유저 정보 조회 API")
    @GetMapping("/info")
    public ResponseDto<UserResponseDto> info(HttpServletRequest request) {
        UserResponseDto userDto = userService.findById(request.getHeader("Authorization"));
        if(userDto == null) {
            throw new ApiException(ErrorCode.NOT_EXIST_USER);
        }
        return ResponseUtil.SUCCESS("유저 정보 조회에 성공하였습니다. ", userDto);
    }

    @PreAuthorize("hasRole('USER')") // 혹은 본인 확인 로직
    @Operation(summary = "계정 삭제", description = "계정 삭제 API")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(Principal principal) {
        try {
            String username = principal.getName(); // 현재 로그인한 사용자의 이름 (혹은 ID)
            userService.deleteUser(username);
            return ResponseEntity.ok("Account successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting account");
        }
    }
}