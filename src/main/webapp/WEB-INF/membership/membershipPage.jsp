<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
				

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>클럽 회원 페이지</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/membershipPage.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
    <main>
   <sec:authorize access="isAuthenticated()" var="principal">
   <sec:authentication property="principal" var="member" />
        <h1>${main.membership.membershipName}</h1>
        <!-- 멤버쉽 수정 -->
        <div>
       	<h2><a href="/updateMembership">정보 수정하기</a></h2>
        </div>
        <h2><a href="/chatserver?membershipCode=${main.membership.membershipCode}">채팅서버가기</a></h2>
        <h2>${main.membership.membershipInfo}</h2>
        <p>인원 현황 : ${membershipUserCount}/${main.membership.membershipMax}</p>
        <h4>호스트 : ${main.member.nickname} 
        </h4>
     
				
        <c:if test="${member.id == main.member.id && membershipUserCount >= main.membership.membershipMax}">
                        	<div>최대 인원에 도달하였습니다. 최대인원을 다시 설정후 확인해줏비시오</div>
                        </c:if>
		
        <c:forEach items="${allMember}" var="cMember">
            <div class="memberTable">
            
                <c:choose>
                    <c:when test="${cMember.listGrade == 'guest'}">
                        <ul> 
                            <li>${cMember.member.nickname} - 가입 대기중</li>
                            <li><img class="allmemberImg" src="${cMember.member.memberImg}" alt="회원 이미지"></li>
                            <c:if test="${main.member.id == member.id && !(membershipUserCount >= main.membership.membershipMax)}">
                                <form id="agreefrm">
                                    <input type="hidden" name="id" value="${cMember.member.id}">
                                    <input type="hidden" name="listGrade" value="regular">
                                    <input type="hidden" name="membershipCode" value="${main.membership.membershipCode}">
                                    <button id="agreeMember">가입 승인</button>
                                </form>
                            </c:if>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <ul> 
                            <li>${cMember.member.nickname}</li>
                            <li><img class="allmemberImg" src="http://192.168.10.51:8081/member/${cMember.member.id}/${cMember.member.memberImg}" alt="회원 이미지"></li>
                            <li>${cMember.listGrade}</li>
                        </ul>
                    </c:otherwise>
                </c:choose>
                 
            </div>
        </c:forEach>
        <a href="/" id="toIndex">메인페이지로 가기</a>
        </sec:authorize>
        
    </main>
    <script src="${pageContext.request.contextPath}/js/membershipPage.js"></script>
</body>
</html>
