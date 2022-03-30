package com.make.plan.repository;

import com.make.plan.dto.FriendDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query(value = "select request from friend where response = :data", nativeQuery = true)
    String userSearching(String data);


//    @Query(value = "select * from User where id Like '%':id'%'", nativeQuery = true)
//    public List<User> userSearching(String id);
//
//    @Query(value = "select * from User where nick Like '%':nick'%'", nativeQuery = true)
//    public List<User> userSearching_NICK(String nick);

//    // 친구 요청 승인, 거절
//    // response_u 의 값은 session에서 user의 code를 찾아와서 대입
//    // Friend Entity에서 request_u, response_u 를 User 클래스의 형태로 받았기에 값을 넘길 때 도 User 클래스 형태로 넘겨야 함
//    @Query(value = "update Friend set status = :status where request_u = :request_u and response_u = :session")
//    @Modifying
//    @Transactional
//    public void requestAccept(String status, HttpSession session, User response_u);
//
//    // 친구 목록
//    @Query(value = "select u.nick from User u inner join Friend f on f.response = u.code where f.response = :response and f.status = '수락'", nativeQuery = true)
//    public List<String> friendList(Long response);
//
//    @Query(value = "select u.nick from Friend f inner join User u on f.response = u.code where f.response = :response and f.status = '대기' ", nativeQuery = true)
//    public List<String> requestFriendAdd(Long response);



}
