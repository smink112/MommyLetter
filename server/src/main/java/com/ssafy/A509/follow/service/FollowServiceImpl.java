package com.ssafy.A509.follow.service;

import com.ssafy.A509.account.model.User;
import com.ssafy.A509.account.repository.AccountRepository;
import com.ssafy.A509.follow.dto.FollowRequestDTO;
import com.ssafy.A509.follow.dto.FollowerListResponseDTO;
import com.ssafy.A509.follow.dto.FollowingListResponseDTO;
import com.ssafy.A509.follow.model.Follow;
import com.ssafy.A509.follow.repository.FollowRepository;
import com.ssafy.A509.profile.dto.BoardProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final AccountRepository accountRepository;

    public FollowServiceImpl(FollowRepository followRepository, AccountRepository accountRepository) {
        this.followRepository = followRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void followUser(Long followerUserId, FollowRequestDTO followRequestDTO) {
        User follower = accountRepository.findById(followerUserId)
                .orElseThrow(() -> new NotFoundException("Follower not found with id: " + followerUserId));

        User followingUser = accountRepository.findById(followRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User to follow not found with id: " + followRequestDTO.getUserId()));

        if (followerUserId.equals(followRequestDTO.getUserId())) {
            throw new IllegalArgumentException("Cannot follow yourself.");
        }

        if (isAlreadyFollowing(followerUserId, followRequestDTO.getUserId())) {
            throw new IllegalArgumentException("Already following the user.");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(followingUser);
        followRepository.save(follow);
    }

    @Override
    public void unfollowUser(Long followerUserId, FollowRequestDTO followRequestDTO) {
        Follow follow = followRepository.findByFollowerUserIdAndFollowingUserId(followerUserId, followRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Not following the user."));

        followRepository.delete(follow);
    }

    @Override
    public FollowerListResponseDTO getFollowerList(Long userId) {
        List<Follow> followerEntities = followRepository.findByFollowingUserId(userId);

        List<BoardProfileResponse> followers = followerEntities.stream()
                .map(follow -> {
                    User followerUser = follow.getFollower();
                    BoardProfileResponse userProfileCardsResponse = new BoardProfileResponse();
                    userProfileCardsResponse.setUserId(followerUser.getUserId());
                    userProfileCardsResponse.setNickname(followerUser.getNickname());

                    // 프로필 사진이 있는 경우에만 설정
                    if (followerUser.getProfile() != null && followerUser.getProfile().getProfilePhoto() != null) {
                        userProfileCardsResponse.setProfilePhoto(followerUser.getProfile().getProfilePhoto());
                    }

                    return userProfileCardsResponse;
                })
                .collect(Collectors.toList());

        return new FollowerListResponseDTO(followers);
    }


    @Override
    public FollowingListResponseDTO getFollowingList(Long userId) {
        List<Follow> followingEntities = followRepository.findByFollowerUserId(userId);

        List<BoardProfileResponse> following = followingEntities.stream()
                .map(follow -> {
                    User followingUser = follow.getFollowing();
                    BoardProfileResponse userProfileCardsResponse = new BoardProfileResponse();
                    userProfileCardsResponse.setUserId(followingUser.getUserId());
                    userProfileCardsResponse.setNickname(followingUser.getNickname());

                    // 프로필 사진이 있는 경우에만 설정
                    if (followingUser.getProfile() != null && followingUser.getProfile().getProfilePhoto() != null) {
                        userProfileCardsResponse.setProfilePhoto(followingUser.getProfile().getProfilePhoto());
                    }

                    return userProfileCardsResponse;
                })
                .collect(Collectors.toList());

        return new FollowingListResponseDTO(following);
    }


    private boolean isAlreadyFollowing(Long followerUserId, Long followingUserId) {
        return followRepository.findByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId).isPresent();
    }
}