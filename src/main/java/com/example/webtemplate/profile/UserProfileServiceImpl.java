package com.example.webtemplate.profile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.webtemplate.common.response.StandardResponse.QueryResponse;
import com.example.webtemplate.profile.UserProfileResponsesDto.UpdateProfileResponse;
import com.example.webtemplate.profile.UserProfileResponsesDto.UserProfileDto;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository repository;

    UserProfileServiceImpl(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Long> getAllUserProfileIds() {
        return repository.findAll().stream()
                .map(UserProfile::getId)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public UserProfileDto findUserProfileIdsById(Long id) {
        UserProfile entity = repository.findById(id).orElseThrow();
        return entity.convertToDto();
    }

    @Override
    public UpdateProfileResponse updateUserProfile(
            Long id,
            Optional<String> nickname,
            Optional<String> profileUrl,
            Optional<String> bio,
            Optional<Boolean> accountVisible,
            Optional<Boolean> followerVisible) {

        UserProfile entity = repository.findById(id).orElseThrow();

        nickname.ifPresent(entity::setNickname);
        profileUrl.ifPresent(entity::setProfileUrl);
        bio.ifPresent(entity::setBio);
        accountVisible.ifPresent(entity::setAccountVisible);
        followerVisible.ifPresent(entity::setFollowerVisible);

        entity = repository.save(entity);

        return new UpdateProfileResponse(findUserProfileIdsById(id), entity.getUpdatedAt());
    }



    @Override
    public QueryResponse<UserProfileDto> findUserProfilesByNickname(String nickname, String sort, int limit, int page) {

        // [ ] 리팩토링 필요 - 복잡도 낮추기

        Example<UserProfile> example = createNicknameSearchExample(nickname);
        PageRequest pageRequest = PageRequest.of(page, limit, parseSort(sort));
        Page<UserProfile> userProfilePage = repository.findAll(example, pageRequest);

        Page<UserProfileDto> userProfileDtoPage = userProfilePage.map(profile -> {
            return profile.convertToDto();
        });

        List<UserProfileDto> items = userProfileDtoPage.getContent();
        return QueryResponse.of(items, userProfileDtoPage, limit);
    }

    /**
     * 닉네임으로 UserProfile을 검색하기 위한 Example 객체를 생성합니다.
     * 검색은 대소문자를 구분하지 않으며 닉네임의 일부만 일치해도 됩니다.
     * 다른 필드는 검색에서 무시됩니다.
     *
     * @param nickname 검색할 닉네임
     * @return 검색을 위한 Example 객체
     */
    private Example<UserProfile> createNicknameSearchExample(String nickname) {
        UserProfile probe = new UserProfile();
        probe.setNickname(nickname);

        // [ ] 리팩토링 필요 - 복잡도 낮추기 - JPQL로 변경 고려
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("nickname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("accountVisible", "followerVisible", "bio", "profileUrl", "account")
                .withIgnoreNullValues();

        return Example.of(probe, matcher);
    }

    /**
     * 정렬 문자열을 Sort 객체로 파싱합니다.
     * 정렬 문자열은 쉼표로 구분된 속성 목록이며, 각 속성 뒤에는 선택적으로 ":asc" 또는 ":desc"가 올 수 있습니다.
     * 정렬 문자열이 제공되지 않으면 기본적으로 "nickname"을 기준으로 오름차순 정렬합니다.
     *
     * @param sortStr 정렬 문자열
     * @return Sort 객체
     */
    private Sort parseSort(String sortStr) {
        if (sortStr == null || sortStr.trim().isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "nickname");
        }

        List<Sort.Order> orders = Arrays.stream(sortStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::parseOrderToken)
                .collect(Collectors.toList());

        return Sort.by(orders);
    }

    /**
     * 단일 정렬 토큰을 Sort.Order 객체로 파싱합니다.
     * 토큰은 "속성:방향" 또는 "속성" 형식이 될 수 있습니다.
     * 방향이 지정되지 않으면 기본적으로 오름차순으로 정렬됩니다.
     *
     * @param token 정렬 토큰
     * @return Sort.Order 객체
     */
    private Sort.Order parseOrderToken(String token) {
        if (token.contains(":")) {
            String[] parts = token.split(":");
            String property = parts[0].trim();
            String direction = parts[1].trim().toLowerCase();

            return direction.equals("desc") ? Sort.Order.desc(property) : Sort.Order.asc(property);
        }
        return Sort.Order.asc(token.trim());
    }
}
