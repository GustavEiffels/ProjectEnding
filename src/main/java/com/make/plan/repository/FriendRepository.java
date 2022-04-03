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
import java.util.Map;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // request --> 요청하는 유저 코드 => session에서 값 받아옴, response --> 친구 요청을 받는 유저 코드
    @Transactional
    @Modifying
    @Query(value = "insert into Friend (request, response) values (:myCode, :code)", nativeQuery = true)
    int friendAdd(User myCode, User code);

    // 친구 요청 승인, 거절
    // response_u 의 값은 session에서 user의 code를 찾아와서 대입
    @Query(value = "update Friend set status = :status where request_u = :friendCode and response_u = :myCode")
    @Modifying
    @Transactional
    int friendInfoUpdate(User myCode, String status , User friendCode);

    // 친구 목록
    @Query(value = "select (select u.code from User u where u.code = f.request) as code, (select u.nick from User u where u.code = f.request) as nickname, f.status from User u inner join Friend f on f.response = u.code where f.response = :response and f.status = '수락'", nativeQuery = true)
    List<Map<String, Object>> friendList(Long response);

    // 유저한테 친구 추가 요청 들어온 목록
    @Query(value = "select f.status, u.nick, f.regdate from Friend f inner join User u on f.response = u.code where f.response = :response and f.status = '대기' ", nativeQuery = true)
    List<String> requestFriendAdd(Long response);

    // 유저한테 친구 추가 요청 들어온 목록
    @Query(value = "select (select u.code from User u where u.code = f.request) as code, f.status, (select u.nick from user u where u.code = f.request) as nickname, f.regdate from Friend f inner join User u on f.response = u.code where f.response = :response and f.status = '대기' ", nativeQuery = true)
    List<Map<String, Object>> requestFriendAddMapType(Long response);



}
